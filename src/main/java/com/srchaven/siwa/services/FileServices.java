package com.srchaven.siwa.services;

import com.srchaven.siwa.model.AggregatedObservations;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;

/**
 * Service endpoint that handles various file operations. 
 */
//TODO: Valdiation of the directories
public class FileServices
{
    /** The logger for this class */
    private static final Logger LOGGER = Logger.getLogger(FileServices.class);

    /** Pattern for the {@code SimpleDateFormat} to create the filename timestamp */
    private static final String TIMESTAMP_FORMAT = "yyyyMMdd'_'HHmmssSSS";

    /** Separator between filename and timestamp */
    private static final String FILENAME_TIMESTAMP_SEPARATOR = "__";

    /** Reference to the processing directory */
    private File processingDirectory;

    /** Reference to the archival directory */
    private File archiveDirectory;

    /** Reference to the error logging directory */
    private File errorDirectory;

    /**
     * Setter.
     *
     * @param path the absolute path to the processing directory.
     */
    public void setProcessingDirectory(String path)
    {
        processingDirectory = new File(path);
    }

    /**
     * Setter.
     *
     * @param archiveDirPath the absolute path to the archival directory.
     */
    public void setArchiveDirectory(String archiveDirPath)
    {
        archiveDirectory = new File(archiveDirPath);
    }

    /**
     * Setter.
     *
     * @param path the absolute path to the error directory.
     */
    public void setErrorDirectory(String path)
    {
        errorDirectory = new File(path);
    }

    /**
     * Applies a timestamp to the file. The provided file will be renamed in the form:
     * [Original filename]__YYYYMMDD_HHMMSSmmm.[Orig extension]
     *
     * NOTE: Extension is limited to a maximum of four characters.
     *
     * @param file the file to add a timestamp to. Cannot be {@code null}. Must be a readable, writeable file with a
     * 		valid parent directory.
     *
     * @return a reference to the renamed file
     *
     * @throws IllegalArgumentException if the parameter is invalid
     */
    public File addTimestampToFilename(File file)
    {
        assertFileValid(file);

        String origFilename = file.getName();

        StringBuilder newFilename = new StringBuilder(origFilename);
        int timestampInsertLoc;

        //If the file ends in a dot followed by one to four letters or numbers (i.e. if it has an extension)
        if (origFilename.matches(".*\\.\\w{1,4}$"))
        {
            timestampInsertLoc = origFilename.lastIndexOf(".");
        }
        else
        {
            timestampInsertLoc = newFilename.length();
        }

        newFilename.insert(timestampInsertLoc, FILENAME_TIMESTAMP_SEPARATOR);
        timestampInsertLoc += FILENAME_TIMESTAMP_SEPARATOR.length();

        String timestamp = new SimpleDateFormat(TIMESTAMP_FORMAT).format(new Date());

        newFilename.insert(timestampInsertLoc, timestamp);

        File timestampedFile = new File(file.getParentFile(), newFilename.toString());
        file.renameTo(timestampedFile);

        return timestampedFile;
    }

    /**
     * Moves a file from its current location to the processing directory and outputs a reference to the file at its
     * new location.
     *
     * @param file the file to move to the processing directory. Must be a readable, writeable file with a valid parent
     * 		directory.
     *
     * @return a reference to the file in its new location (in the processing directory).
     *
     * @throws IllegalArgumentException if the parameter is invalid
     */
    public File moveFileToProcessingDirectory(File file)
    {
        assertFileValid(file);

        File finalStagedFile = new File(processingDirectory, file.getName());
        File dotStagedFile = new File(processingDirectory, "." + file.getName());
        file.renameTo(dotStagedFile);
        dotStagedFile.renameTo(finalStagedFile);
//TODO: Error handling for failure

        LOGGER.info("Staged file from " + file.getPath() + " to " + dotStagedFile.getPath());

        return dotStagedFile;
    }

    /**
     * Copies the inputed file into the archival directory.
     *
     * @param file the file to archive. Must be a readable, writeable file with a valid parent directory.
     *
     * @return a reference to the original file's location (identical to {@code _file})
     *
     * @throws Exception if copying the file fails.
     * @throws IllegalArgumentException if the parameter is invalid
     */
    public File copyFileToArchiveDirectory(File file) throws Exception
    {
        assertFileValid(file);

        File archivedFile = new File(archiveDirectory, file.getName());
        FileUtils.copyFile(file, archivedFile);
//TODO: error handling for failure

        LOGGER.info("Archived file from " + file.getPath() + " to " + archivedFile.getPath());

        return file; // return original file because that's what will be processed
    }

    /**
     * Deletes an input file in the processing directory. The filename to be deleted will be extracted from the
     * aggregated observation object.
     *
     * @param aggObs the aggregated observations associated with the file being deleted.
     *
     * @return the inputed list of records
     */
    public AggregatedObservations deleteSourceFile(AggregatedObservations aggObs)
    {
//TODO: Input validation
//TODO: Make sure all records are from the same file (?)
        String filename = aggObs.getFilename();
        new File(processingDirectory, filename).delete();
        LOGGER.trace("All records received that originated in " + filename + ". Deleted.");
        return aggObs;
    }

    /**
     * Moves a file to the error directory. Used in the event that a file is not processable.
     *
     * @param records
     *
     * @return
     */
    public List<Observation> moveFileToErrorDirectory(List<Observation> records)
    {
//TODO: Input validation
        // Get the filename where the incomplete set of records originated
        String filename = records.get(0).getFilename();
        LOGGER.trace("Received incomplete set of records that originated in " + filename + ".");

        // move file with problems to error directory
        File fileInProcessingDirectory = new File(processingDirectory, filename);
        File fileInErrorDirectory = new File(errorDirectory, filename);
        fileInProcessingDirectory.renameTo(fileInErrorDirectory);
//TODO: error handling for failure

        LOGGER.info("Moved file with errors from " + fileInProcessingDirectory.getPath() + " to "
                + fileInErrorDirectory.getPath());

        return records;
    }

    /**
     * Asserts that a file is valid. (See the {@code file} parameter for more details)
     *
     * @param file the file to assert the validity of. Cannot be {@code null}. Must be a readable, writeable file with a
     * 		valid parent directory.
     *
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    private void assertFileValid(File file)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("Null file is invalid");
        }
        else if (!file.isFile())
        {
            throw new IllegalArgumentException("File object did not reference a valid file");
        }
        else if (!file.canRead())
        {
            throw new IllegalArgumentException("File must be readable");
        }
        else if (!file.canWrite())
        {
            throw new IllegalArgumentException("File must be writable");
        }
        else if (!file.getParentFile().isDirectory())
        {
            throw new IllegalArgumentException("File must be inside a valid directory");
        }
    }
}

package com.srchaven.siwa.services;

import java.io.File;
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
	 * Moves a file from its current location to the processing directory and outputs a reference to the file at its
	 * new location.
	 * 
	 * @param file the file to move to the processing directory
	 * 
	 * @return a reference to the file in its new location (in the processing directory).
	 */
	@Transformer
	public File moveFileToProcessingDirectory(File file)
	{
		File stagedFile = new File(processingDirectory, file.getName());
		file.renameTo(stagedFile);
//TODO: Error handling for failure

		LOGGER.info("Staged file from " + file.getPath() + " to " + stagedFile.getPath());
		
		return stagedFile;
	}

	/**
	 * Copies the inputed file into the archival directory.
	 * 
	 * @param file the file to archive.
	 * 
	 * @return a reference to the original file's location (identical to {@code _file})
	 * 
	 * @throws Exception if copying the file fails.
	 */
	@Transformer
	public File copyFileToArchiveDirectory(File file) throws Exception
	{
		File archivedFile = new File(archiveDirectory, file.getName());
		FileUtils.copyFile(file, archivedFile);
//TODO: error handling for failure

		LOGGER.info("Archived file from " + file.getPath() + " to " + archivedFile.getPath());

		return file; // return original file because that's what will be processed
	}

	/**
	 * Deletes an input file in the processing directory. The {@code filename} field from the first {@code Record} in
	 * the provided list will be extracted and used as filename of the file to be deleted. Thus, the path to the deleted
	 * file will be:
	 * [Processing directory]/[First record's filename] 
	 * 
	 * @param records a list of the records associated with the file being deleted.
	 * 
	 * @return the inputed list of records
	 */
	public List<Observation> deleteSourceFile(List<Observation> records)
	{
//TODO: Make sure all records are from the same file (?)
		String filename = records.get(0).getFilename();
		new File(processingDirectory, filename).delete();
		LOGGER.trace("All records received that originated in " + filename + ". Deleted.");
		return records;
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
		// get the filename where the incomplete set of records originated
		String filename = records.get(0).getFilename();
		LOGGER.trace("Received incomplete set of records that originated in " + filename + ".");

		// move file with problems to error directory
		File fileInProcessingDirectory = new File(processingDirectory, filename);
		File fileInErrorDirectory = new File(errorDirectory, filename);
		fileInProcessingDirectory.renameTo(fileInErrorDirectory);
		// TODO error handling for failure
		
		LOGGER.info("Moved file with errors from " + fileInProcessingDirectory.getPath() + " to "
				+ fileInErrorDirectory.getPath());

		return records;
	}
}

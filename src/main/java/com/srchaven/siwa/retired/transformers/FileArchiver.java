package com.srchaven.siwa.retired.transformers;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

/**
 * Transformer that moves the inputed file into the archival directory and outputs a reference to the original file.
 */
//TODO: Bean validation
//TODO: Change from transformer to service/service activator. Possibly collapse all file utilties into a single bean?
public class FileArchiver
{
    /** The logger for this class */
    private static final Logger LOGGER = Logger.getLogger(FileArchiver.class);

    private File archiveDirectory;

    /**
     * Setter.
     *
     * @param _archiveDirPath the absolute path to the archival directory.
     */
    public void setArchiveDirectory(String _archiveDirPath)
    {
        archiveDirectory = new File(_archiveDirPath);
    }

    /**
     * Moves the inputed file into the archival directory and outputs a reference to the file's new location.
     *
     * @param _file the file to archive.
     *
     * @return a reference to the original file's location (identical to {@code _file})
     *
     * @throws Exception if copying the file fails.
     */
    @Transformer
    public File archive(File _file) throws Exception
    {
        File archivedFile = new File(archiveDirectory, _file.getName());
        FileUtils.copyFile(_file, archivedFile);
//TODO: error handling for failure

        LOGGER.info("Archived file from " + _file.getPath() + " to " + archivedFile.getPath());

        return _file; // return original file because that's what will be processed
    }
}

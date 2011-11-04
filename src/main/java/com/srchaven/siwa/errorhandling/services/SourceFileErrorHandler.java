package com.srchaven.siwa.errorhandling.services;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import org.springframework.integration.annotation.ServiceActivator;

// TODO: Bean validation
// TODO: JavaDoc
public class SourceFileErrorHandler
{
    /** The logger for this class */
    private static final Logger LOGGER = Logger.getLogger(SourceFileErrorHandler.class);

    /** Reference to the processing directory */
    private File processingDirectory;

    /** Reference to the error logging directory */
    private File errorDirectory;

    /**
     * Setter.
     *
     * @param _path the absolute path to the processing directory.
     */
    public void setProcessingDirectory(String _path)
    {
        processingDirectory = new File(_path);
    }

    /**
     * Setter.
     *
     * @param _path the absolute path to the error directory.
     */
    public void setErrorDirectory(String _path)
    {
        errorDirectory = new File(_path);
    }

    @ServiceActivator
    public ErrorDetails receive(ErrorDetails errDet)
    {

        // get the filename where the incomplete set of records originated
        Object failedPayload = errDet.getFailedMessage().getPayload();

        if (!(failedPayload instanceof File))
        {
            LOGGER.error("Message with paylod of type \"" + failedPayload.getClass().getCanonicalName()
                    + "\" received on source file error channel, but expected type was File.");
            return errDet;
        }

        String filename = ((File) failedPayload).getName();
        LOGGER.error("Error processing file \"" + filename + "\". See error file for more details.");

        // move file with problems to error directory
        File fileInProcessingDirectory = new File(processingDirectory, filename);
        File fileInErrorDirectory = new File(errorDirectory, filename);
        fileInProcessingDirectory.renameTo(fileInErrorDirectory);

        if (fileInProcessingDirectory.exists())
        {
            LOGGER.fatal("Unable to move file \"" + fileInProcessingDirectory.getAbsolutePath() + "\" to \""
                    + fileInErrorDirectory.getAbsolutePath() + "\". Exiting.");
            System.exit(1);
        }

        if (!fileInErrorDirectory.exists())
        {
            LOGGER.fatal("File \"" + fileInProcessingDirectory.getAbsolutePath() + "\" went missing while moving to \""
                    + fileInErrorDirectory.getAbsolutePath() + "\". Exiting.");
            System.exit(1);
        }

        LOGGER.info("Moved file with errors from " + fileInProcessingDirectory.getAbsolutePath() + " to "
                + fileInErrorDirectory.getAbsolutePath());

        File errLog = new File(fileInErrorDirectory.getParentFile(), fileInErrorDirectory.getName() + ".errlog");

        FileWriter errLogWriter = null;

        try
        {
            errLogWriter = new FileWriter(errLog);

            errLogWriter.write(errDet.generateErrorDescription());
        } catch (IOException error)
        {
            LOGGER.error("Error writing error log. Exception: " + error.getMessage());
        }
        finally
        {
            if (errLogWriter != null)
            {
                try
                {
                    errLogWriter.close();
                } catch (IOException error)
                {
                    LOGGER.error("Error closing IO stream while writing error log. Exception: " + error.getMessage());
                }
            }
        }

        return errDet;
    }
}

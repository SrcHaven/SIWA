package com.srchaven.siwa.services;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import com.srchaven.siwa.model.AggregatedObservations;
import com.srchaven.siwa.model.Observation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.integration.annotation.ServiceActivator;

/**
 *
 */
public class BadObservationOutputter
{
    private static final Logger LOGGER = Logger.getLogger(BadObservationOutputter.class);

    /** Reference to the error logging directory */
    private File errorDirectory;

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
    public AggregatedObservations output(AggregatedObservations aggObs)
    {
        if (!aggObs.hasBadObservations())
        {
            return aggObs;
        }

        List<ErrorDetails> errorDetailsList = aggObs.getBadObservations();

        String sourceFilename = aggObs.getFilename();

        File origFile = new File(sourceFilename);

        if (!origFile.isFile())
        {
            LOGGER.fatal("File \"" + origFile.getAbsolutePath() + "\" went missing during processing.");
            System.exit(1);
        }

        File errLog = new File(errorDirectory, origFile.getName() + ".errlog");

        FileWriter errLogWriter = null;

        BufferedWriter buffWriter = null;

        try
        {
            errLogWriter = new FileWriter(errLog);

            buffWriter = new BufferedWriter(errLogWriter);

            for (ErrorDetails errDet : errorDetailsList)
            {
                buffWriter.write(errDet.generateErrorDescription());
            }
        } catch (IOException error)
        {
            LOGGER.error("Error writing error log. Exception: " + error.getMessage());
        }
        finally
        {
            if (buffWriter != null)
            {
                try
                {
                    buffWriter.close();
                } catch (IOException error)
                {
                    LOGGER.error("Error closing IO stream while writing error log. Exception: " + error.getMessage());
                }
            }

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

        return aggObs;
    }
}

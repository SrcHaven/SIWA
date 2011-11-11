package com.srchaven.siwa.services;

import com.srchaven.siwa.model.AggregatedObservations;
import com.srchaven.siwa.model.ErrorObservation;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.annotation.ServiceActivator;

/**
 *
 */
public class BadObservationOutputter
{
    private static final Logger LOGGER = Logger.getLogger(BadObservationOutputter.class);

    /** Reference to the error logging directory */
    private File errorDirectory;

    /** Flag that indicates if error messages should have error details included. */
    private boolean outputErrorDetails = false;
    
    /** File services--used to move files with errors into the error directory */
    @Autowired
    private FileServices fileServices;

    /**
     * Setter.
     * 
     * @param outputErrorDetails flag that indicates if error messages should have error details included.
     */
    public void setOutputErrorDetails(boolean outputErrorDetails)
    {
        this.outputErrorDetails = outputErrorDetails;
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
    public AggregatedObservations output(AggregatedObservations aggObs)
    {
        if (!aggObs.hasBadObservations())
        {
            return aggObs;
        }

        List<ErrorObservation> badObsList = aggObs.getBadObservations();

        String sourceFilename = aggObs.getFilename();

        File origFile = new File(sourceFilename);

        if (!origFile.isFile())
        {
            LOGGER.fatal("File \"" + origFile.getAbsolutePath() + "\" went missing during processing.");
            System.exit(1);
        }
        
        writeErrorFile(origFile, badObsList);
        
        if (outputErrorDetails)
        {
            writeErrorDetails(origFile, badObsList);
        }
        
        //NOTE: This is the last operation so that if there is a problem during the move we can still try to reconstruct
        // the file from the archive directory (because at least the .errors and possibly .error_details files will be
        // written prior to this call).
        fileServices.moveFileToErrorDirectory(sourceFilename);

        return aggObs;
    }
    
    private void writeErrorFile(File origFile, List<ErrorObservation> badObsList)
    {
        File errFile = new File(errorDirectory, origFile.getName() + ".errors");
        FileWriter errFileWriter = null;
        BufferedWriter buffErrFileWriter = null;
        
        try
        {
            errFileWriter = new FileWriter(errFile);
            buffErrFileWriter = new BufferedWriter(errFileWriter);

            for (ErrorObservation currBadObs : badObsList)
            {
                buffErrFileWriter.append(Integer.toString(currBadObs.getLineNumber()));
                buffErrFileWriter.newLine();
            }
        } catch (IOException error)
        {
            LOGGER.error("Error writing error file. Exception: " + error.getMessage());
        }
        finally
        {
            if (buffErrFileWriter != null)
            {
                try
                {
                    buffErrFileWriter.close();
                } catch (IOException error)
                {
                    LOGGER.error("Error closing IO stream while writing error log. Exception: " + error.getMessage());
                }
            }
            if (errFileWriter != null)
            {
                try
                {
                    errFileWriter.close();
                } catch (IOException error)
                {
                    LOGGER.error("Error closing IO stream while writing error log. Exception: " + error.getMessage());
                }
            }
        }
    }
    
    private void writeErrorDetails(File origFile, List<ErrorObservation> badObsList)
    {
        File errDetails = new File(errorDirectory, origFile.getName() + ".error_details");
        FileWriter errDetailsWriter = null;
        BufferedWriter buffErrDetailsWriter = null;

        try
        {
            errDetailsWriter = new FileWriter(errDetails);
            buffErrDetailsWriter = new BufferedWriter(errDetailsWriter);

            for (ErrorObservation currBadObs : badObsList)
            {
                buffErrDetailsWriter.append("@@@START_RECORD@@@");
                buffErrDetailsWriter.newLine();
                
                buffErrDetailsWriter.append("Details for error on line: ");
                buffErrDetailsWriter.append(Integer.toString(currBadObs.getLineNumber()));
                buffErrDetailsWriter.newLine();
                
                buffErrDetailsWriter.append(currBadObs.getErrorDescription());
                buffErrDetailsWriter.newLine();
                
                buffErrDetailsWriter.append("@@@END_RECORD@@@");
                buffErrDetailsWriter.newLine();
                buffErrDetailsWriter.newLine();
            }
        } catch (IOException error)
        {
            LOGGER.error("Error writing error details. Exception: " + error.getMessage());
        }
        finally
        {
            if (buffErrDetailsWriter != null)
            {
                try
                {
                    buffErrDetailsWriter.close();
                } catch (IOException error)
                {
                    LOGGER.error("Error closing IO stream while writing error log. Exception: " + error.getMessage());
                }
            }
            if (errDetailsWriter != null)
            {
                try
                {
                    errDetailsWriter.close();
                } catch (IOException error)
                {
                    LOGGER.error("Error closing IO stream while writing error log. Exception: " + error.getMessage());
                }
            }
        }
    }
}

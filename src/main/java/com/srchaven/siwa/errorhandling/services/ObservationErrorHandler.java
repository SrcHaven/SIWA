package com.srchaven.siwa.errorhandling.services;

import org.apache.log4j.Logger;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import com.srchaven.siwa.model.AbstractObservation;
import com.srchaven.siwa.model.ErrorObservation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;

// TODO: JavaDoc
public class ObservationErrorHandler
{
    /** The logger for this class */
    private static final Logger LOGGER = Logger.getLogger(ObservationErrorHandler.class);

    private boolean outputErrorDetails = false;

    /**
     * Setter.
     * 
     * @param outputErrorDetails flag that indicates if error messages should have error details included.
     */
    public void setOutputErrorDetails(boolean outputErrorDetails)
    {
        this.outputErrorDetails = outputErrorDetails;
    }

    @ServiceActivator
    public Message<List<ErrorObservation>> receive(ErrorDetails errDet)
    {
        // Get the source string that failed
        Object failedPayload = errDet.getFailedMessage().getPayload();

        //NOTE: Can't do instanceof List<AbstractObservation> thanks to reification of types
        if (!(failedPayload instanceof List))
        {
            LOGGER.fatal("Message with paylod of type \"" + failedPayload.getClass().getCanonicalName()
                    + "\" received on source file error channel, but expected a type \"List<AbstractObservation>\".");
            
//TODO: This probably isn't the best way to handle this.
            System.exit(1);
        }

        List<AbstractObservation> failedObsList = (List<AbstractObservation>) failedPayload;
        LOGGER.error("Error processing observations from file \"" + failedObsList.get(0).getFilename() + "\".");

        List<ErrorObservation> errorObsList = new ArrayList(failedObsList.size());
        
        for (AbstractObservation currErrObs : failedObsList)
        {
            if (outputErrorDetails)
            {
                errorObsList.add(new ErrorObservation(currErrObs, errDet.generateErrorDescription()));
            }            
            else
            {
                errorObsList.add(new ErrorObservation(currErrObs, ""));
            }
        }
        
        return MessageBuilder.withPayload(errorObsList).setHeader(FileHeaders.FILENAME,
                failedObsList.get(0).getFilename()).build();
    }

}

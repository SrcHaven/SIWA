package com.srchaven.siwa.errorhandling.services;

import org.apache.log4j.Logger;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import com.srchaven.siwa.model.AbstractObservation;
import com.srchaven.siwa.model.ErrorObservation;
import org.springframework.integration.annotation.ServiceActivator;

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
    public ErrorObservation receive(ErrorDetails errDet)
    {
        // Get the source string that failed
        Object failedPayload = errDet.getFailedMessage().getPayload();

        if (!(failedPayload instanceof AbstractObservation))
        {
            LOGGER.fatal("Message with paylod of type \"" + failedPayload.getClass().getCanonicalName()
                    + "\" received on source file error channel, but expected a type the implemented "
                    + "ObservationSourceStringCarrier.");
            
//TODO: This probably isn't the best way to handle this.
            System.exit(1);
        }

        AbstractObservation failedObs = (AbstractObservation) failedPayload;
        LOGGER.error("Error processing observation from line " + failedObs.getLineNumber() + " from file \""
                + failedObs.getFilename() + "\".");

        ErrorObservation errObs;
        if (outputErrorDetails)
        {
            errObs = new ErrorObservation(failedObs, errDet.generateErrorDescription());
        }            
        else
        {
            errObs = new ErrorObservation(failedObs, "");
        }
        
        return errObs;
    }

}

package com.srchaven.siwa.errorhandling.services;

import org.apache.log4j.Logger;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import com.srchaven.siwa.model.ObservationSourceStringCarrierIF;
import org.springframework.integration.annotation.ServiceActivator;

// TODO: JavaDoc
public class ObservationErrorHandler
{
    /** The logger for this class */
    private static final Logger LOGGER = Logger.getLogger(ObservationErrorHandler.class);

    @ServiceActivator
    public ErrorDetails receive(ErrorDetails errDet)
    {
        // Get the source string that failed
        Object failedPayload = errDet.getFailedMessage().getPayload();

        if (!(failedPayload instanceof ObservationSourceStringCarrierIF))
        {
            LOGGER.error("Message with paylod of type \"" + failedPayload.getClass().getCanonicalName()
                    + "\" received on source file error channel, but expected a type the implemented "
                    + "ObservationSourceStringCarrier.");
            return errDet;
        }


        ObservationSourceStringCarrierIF failedObs = (ObservationSourceStringCarrierIF) failedPayload;
        LOGGER.error("Error processing observation \"" + failedObs.getSourceString() + "\" from file \""
                + failedObs.getFilename() + "\". See error file for more details.");

        return errDet;
    }
}

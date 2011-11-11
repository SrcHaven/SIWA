package com.srchaven.siwa.transformers.redactors;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;
import java.util.List;

/**
 * Redactor that removes the latitude and longitude from a observation.
 * 
 * NOTE: This only removes the lat/long, so the state of origin will still be available via the filename. 
 */
//TODO: JavaDocs
public class LocationRedactor
{
    private static final Logger LOGGER = Logger.getLogger(LocationRedactor.class);

    public static String REDACTED_LATITUDE = "XXXXXXX";

    public static String REDACTED_LONGITUDE = "XXXXXXX";

    @Transformer
    public List<Observation> redact(List<Observation> observationList)
    {
        for (Observation currObs : observationList)
        {
            redact(currObs);
        }
        
        return observationList;
    }

    @Transformer
    public Observation redact(Observation observation)
    {
        LOGGER.trace("Message before location redaction: " + observation);
        observation.setLatitude(REDACTED_LATITUDE);
        observation.setLongitude(REDACTED_LONGITUDE);
        LOGGER.trace("Message after location redactiont: " + observation);
        return observation;
    }
}

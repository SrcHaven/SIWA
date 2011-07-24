package com.srchaven.siwa.transformers.redactors;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;


/**
 * Redactor that removes the latitude and longitude from a record.
 * 
 * NOTE: This only removes the lat/long, so the state of origin will still be available via the filename. 
 */
public class LocationRedactor {
	
	private static final Logger LOGGER = Logger.getLogger(LocationRedactor.class);
	
	public static String REDACTED_LATITUDE = "XXXXXXX";
	public static String REDACTED_LONGITUDE = "XXXXXXX";
	
	@Transformer
	public Observation redact(Observation record) throws IOException {
		LOGGER.trace("Message before location redaction: " + record);
		record.setLatitude(REDACTED_LATITUDE);
		record.setLongitude(REDACTED_LONGITUDE);
		LOGGER.trace("Message after location redactiont: " + record);
		return record;
	}

}

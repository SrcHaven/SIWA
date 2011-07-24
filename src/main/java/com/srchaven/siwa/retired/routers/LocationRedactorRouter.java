package com.srchaven.siwa.retired.routers;

import org.springframework.integration.annotation.Router;

import com.srchaven.siwa.model.Observation;
import com.srchaven.siwa.util.FileNameUtils;


/**
 * Router that sends all records on to the {@code locationRedactorBypassChannel} channel EXCEPT those that are from
 * California, which go on to the {@code locationRedactorChannel} for location redaction.
 */
public class LocationRedactorRouter {
	
	@Router
	public String route(Observation record) {
		// location information has to be redacted for California only
		if (FileNameUtils.extractState(record.getFilename()).equals("CA")) {
			return "locationRedactorChannel";
		} else {
			return "locationRedactorBypassChannel";
		}
	}

}

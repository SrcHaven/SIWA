package com.srchaven.siwa.retired.routers;

import org.springframework.integration.annotation.Router;

import com.srchaven.siwa.model.Observation;
import com.srchaven.siwa.util.FileNameUtils;


/**
 * Router that sends all records on to the {@code surfReportEnricherChannel} channel to add the surf report EXCEPT those
 * that are from Alaska, which go on to the {@code surfReportBypassChannel}.
 */
public class SurfReportRouter {
	
	@Router
	public String route(Observation record) {
		// no surf report is available for Alaska
		if (!FileNameUtils.extractState(record.getFilename()).equals("AK")) {
			return "surfReportEnricherChannel";
		} else {
			return "surfReportBypassChannel";
		}
	}

}

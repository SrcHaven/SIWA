package com.srchaven.siwa.retired.routers;

import org.springframework.integration.annotation.Router;

import com.srchaven.siwa.model.Observation;
import com.srchaven.siwa.util.FileNameUtils;

/**
 * Router that sends all records on to the {@code golfReportEnricherChanel} channel to have golf report data added
 * EXCEPT those that are from Colorado, which go on to {@code golfReportBypassChannel}.
 */
public class GolfReportRouter
{
    @Router
    public String route(Observation record)
    {
        // no golf report data is available for Colorado
        if (!FileNameUtils.extractState(record.getFilename()).equals("CO"))
        {
            return "golfReportEnricherChannel";
        }
        else
        {
            return "golfReportBypassChannel";
        }
    }
}

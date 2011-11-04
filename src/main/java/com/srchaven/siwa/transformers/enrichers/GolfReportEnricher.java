package com.srchaven.siwa.transformers.enrichers;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;
import com.srchaven.siwa.model.SupplementalReport;
import com.srchaven.siwa.retired.routers.GolfReportRouter;
import com.srchaven.siwa.util.FileNameUtils;

/**
 * Enricher that adds the a fake "golfing quality report" to a record. The golfing quality is based on state, as
 * follows:
 * 
 * <ul>
 * <li>CA: "Golfing is great!"</li>
 * <li>FL: "Golfing is great!"</li>
 * <li>MN: "Golfing is ok!"</li>
 * <li>All others: "No golfing available."
 * </ul>
 */
public class GolfReportEnricher
{
    private static final Logger LOGGER = Logger.getLogger(GolfReportEnricher.class);

    private static final String REPORT_TYPE = "GolfReport";

    @Transformer
    public Observation enrich(Observation observation) throws IOException
    {
        LOGGER.trace("Message before golf enrichment: " + observation);
        String state = FileNameUtils.extractState(observation.getFilename());
        String golfReport = "No golfing available.";
        if (state.equals("CA"))
        {
            golfReport = "Golfing is great!";
        }
        else if (state.equals("FL"))
        {
            golfReport = "Golfing is great!";
        }
        else if (state.equals("MN"))
        {
            golfReport = "Golfing is ok!";
        }
        observation.addSupplementalReport(new SupplementalReport(REPORT_TYPE, golfReport));
        LOGGER.trace("Message after golf enrichment:  " + observation);
        return observation;
    }
}

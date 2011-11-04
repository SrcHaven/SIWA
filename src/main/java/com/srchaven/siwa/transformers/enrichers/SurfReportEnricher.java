package com.srchaven.siwa.transformers.enrichers;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.srchaven.siwa.model.Observation;
import com.srchaven.siwa.model.SupplementalReport;
import com.srchaven.siwa.retired.routers.SurfReportRouter;
import com.srchaven.siwa.util.FileNameUtils;

/**
 * Enricher that adds the a fake "surfing quality report" to a record. The surfing quality is based on state, as
 * follows:
 * 
 * <ul>
 * <li>CA: "Surfing is great!"</li>
 * <li>HI: "Surfing is amazing!"</li>
 * <li>FL: "Surfing is ok!"</li>
 * <li>MN: "Surfing is ok!"</li>
 * <li>All others: "No surfing available."
 * </ul>
 */
// TODO: Why is this a managed resource? I don't see any JMX calls in the class!
@ManagedResource
public class SurfReportEnricher
{
    private static final Logger LOGGER = Logger.getLogger(SurfReportEnricher.class);

    private static final String REPORT_TYPE = "SurfReport";

    private final AtomicInteger counter = new AtomicInteger(0);

    @Transformer
    public Observation enrich(Observation record) throws IOException
    {
        counter.incrementAndGet();
        // LOGGER.trace("Message before surf report enrichment: " + record);
        String state = FileNameUtils.extractState(record.getFilename());
        String surfReport = "No surfing available.";
        if (state.equals("CA"))
        {
            surfReport = "Surfing is great!";
        }
        else if (state.equals("HI"))
        {
            surfReport = "Surfing is amazing!";
        }
        else if (state.equals("FL"))
        {
            surfReport = "Surfing is ok!";
        }
        else if (state.equals("MN"))
        {
            surfReport = "Surfing is ok!";
        }
        record.addSupplementalReport(new SupplementalReport(REPORT_TYPE, surfReport));
        LOGGER.trace(counter.get() + "==" + Thread.currentThread().getName()
                + ":Message after surf report enrichment: " + record);
        // LOGGER.trace("Message after surf report enrichment:  " + record);
        counter.decrementAndGet();
        return record;
    }
}

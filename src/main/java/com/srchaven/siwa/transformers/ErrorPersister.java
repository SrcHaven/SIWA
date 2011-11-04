package com.srchaven.siwa.transformers;

import java.io.File;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;

//TODO: Unused. Delete?
public class ErrorPersister
{
    private static final Logger LOGGER = Logger.getLogger(ErrorPersister.class);

    // TODO make configurable
    private File errorDirectory = new File("C:\\Temp\\SpringIntegrationExample\\error");

    @Transformer
    public Observation persist(Observation record) throws Exception
    {
        File errorFile = new File(errorDirectory, record.getFilename());
        LOGGER.trace("Storing record in error file (" + errorFile.getAbsolutePath() + "): " + record);
        // LOGGER.info("Persisted record to " + databaseDumpFile.getPath());

        return record; // original record so something can look for all records
        // and delete input file
    }
}

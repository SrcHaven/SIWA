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
    public Observation persist(Observation observation) throws Exception
    {
        File errorFile = new File(errorDirectory, observation.getFilename());
        LOGGER.trace("Storing observation in error file (" + errorFile.getAbsolutePath() + "): " + observation);
        // LOGGER.info("Persisted observation to " + databaseDumpFile.getPath());

        return observation; // original observation so something can look for all observations
        // and delete input file
    }
}

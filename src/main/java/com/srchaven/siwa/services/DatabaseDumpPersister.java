package com.srchaven.siwa.services;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.srchaven.siwa.dao.ObservationDaoIF;
import com.srchaven.siwa.model.Observation;

/**
 * Service endpoint used to persists an {@code Observation} object by storing in a database.
 */
public class DatabaseDumpPersister
{
    /** Logger for this class */
    private static final Logger LOGGER = Logger.getLogger(DatabaseDumpPersister.class);

    /** DAO for persisting observations */
    private ObservationDaoIF observationDao;

    /** The number of observations that have been persisted */
    private final AtomicLong persistedCount = new AtomicLong(0);

    /** The message count to cause throw exception on */
    private final long failOnMessageNum = -1; //Set to positive number to cause failure

    /**
     * Setter.
     *
     * @param dao the DAO used to store {@code Observation} objects in the database. Cannot be {@code null}.
     *
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    @Autowired(required = true)
    public void setObservationDao(ObservationDaoIF dao)
    {
        if (dao == null)
        {
            throw new IllegalArgumentException("DAO cannot be null");
        }

        observationDao = dao;
    }

    /**
     * Service that persists an {@code Observation} object by storing in a database.
     *
     * @param obs the {@code Observation} object to persist
     */
    public Observation persist(Observation obs)
    {
        long msgCount = persistedCount.incrementAndGet();
        LOGGER.trace(msgCount + " Persisting: " + obs);
        // LOGGER.info("Persisted record to " + databaseDumpFile.getPath());

        if (msgCount == failOnMessageNum)
        {
            throw new RuntimeException("TEST: Unable to persist record: " + obs);
        }

        observationDao.saveObservation(obs);

        return obs;
    }
}

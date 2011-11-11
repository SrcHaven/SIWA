package com.srchaven.siwa.model;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class AggregatedObservations
{
    private final List<Observation> goodObservations;

    private final List<ErrorObservation> badObservations;

    private final String filename;

    public AggregatedObservations(List<Observation> goodObservations, List<ErrorObservation> badObservations,
            String filename)
    {
        this.goodObservations = new ArrayList<Observation>(goodObservations);
        this.badObservations = new ArrayList<ErrorObservation>(badObservations);
        this.filename = filename;
    }

    public List<Observation> getGoodObservations()
    {
        return Collections.unmodifiableList(goodObservations);
    }

    public List<ErrorObservation> getBadObservations()
    {
        return Collections.unmodifiableList(badObservations);
    }

    public boolean hasBadObservations()
    {
        return !badObservations.isEmpty();
    }

    public String getFilename()
    {
        return filename;
    }
}

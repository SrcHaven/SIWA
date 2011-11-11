package com.srchaven.siwa.model;

import java.util.Collections;
import java.util.List;

/**
 * Carrier for the source String for an Observation. Although we could pass the raw string through the messaging
 * framework, providing our own wrapper allows us to implement the common {@code ObservationSourceStringCarrier} 
 * interface, simplifying error handling.
 */
//TODO: JavaDocs
public class RawObservation extends AbstractObservation
{
    private final List<String> observationFieldVals;

    //NOTE FOR JAVADOCS: No defensive copying for performance reasons
    public RawObservation(String filename, int lineNumber, List<String> observationFieldVals)
    {
        super(filename, lineNumber);
        
        this.observationFieldVals = Collections.unmodifiableList(observationFieldVals);
    }

    public List<String> getObservationFieldVals()
    {
        return observationFieldVals;
    }
}

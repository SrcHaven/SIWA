package com.srchaven.siwa.model;

/**
 * Carrier for the source String for an Observation. Although we could pass the raw string through the messaging
 * framework, providing our own wrapper allows us to implement the common {@code ObservationSourceStringCarrier} 
 * interface, simplifying error handling.
 */
public class RawObservation implements ObservationSourceStringCarrierIF
{
    private final String obsAsString;

    private final String filename;

    public RawObservation(String obs, String filename)
    {
        this.obsAsString = obs;
        this.filename = filename;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSourceString()
    {
        return obsAsString;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilename()
    {
        return filename;
    }
}

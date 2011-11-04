package com.srchaven.siwa.model;

/**
 * Interface for classes that hold the source string for an Observation (the original String that was read from the 
 * input file and later turned into an Observation). This is used to simplify error handling, as the error path can use
 * the source string for most of its needs and not have to operate on a possibly-corrupt Observation.
 */
public interface ObservationSourceStringCarrierIF
{
    /**
     * Getter.
     *
     * @return the original source string for an Observation. (The raw line that was read from the input file.)
     */
    String getSourceString();

    /**
     * Getter
     *
     * @return the absolute path to the file that the Observation was read from
     */
    String getFilename();
}

package com.srchaven.siwa.model;

import javax.persistence.Transient;

/**
 * Abstract base class for all Observations. This is used to simplify error handling, as the error path can use
 * the source string for most of its needs and not have to operate on a possibly-corrupt Observation.
 */
public abstract class AbstractObservation
{
    /**
     * Constructor.
     * 
     * @param filename the filename for the file that this observation was read from. Cannot be {@code null} or empty.
     * @param lineNumber the line number that this observation was read from. Cannot be less than 1.
     * 
     * @throws IllegalArgumentException if any of the parameters are invalid.
     */
    public AbstractObservation(String filename, int lineNumber)
    {
        if (filename == null)
        {
            throw new IllegalArgumentException("Source filename cannot be null");
        }
        else if (filename.isEmpty())
        {
            throw new IllegalArgumentException("Source filename cannot be empty");
        }
        
        if (lineNumber < 1)
        {
            throw new IllegalArgumentException("Line number cannot be less than one. Provided line number was "
                    + lineNumber);
        }

        this.lineNumber = lineNumber;
        this.filename = filename;
    }
    
    /**
     * Copy constructor.
     * 
     * @param sourceObs the {@code AbstractObservation} to copy. Cannot be {@code null}.
     * 
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public AbstractObservation(AbstractObservation sourceObs)
    {
        if (sourceObs == null)
        {
            throw new IllegalArgumentException("Source observation cannot be null");
        }
        
        this.filename = sourceObs.filename;
        this.lineNumber = sourceObs.lineNumber;
    }
    
    /** The filename for the file that this observation was read from */
    protected String filename;

    /** The line number that this observation was read from. */
    protected int lineNumber;
    
    /**
     * Getter.
     * 
     * @return the filename for the file that this observation was read from.
     */
    @Transient
    public String getFilename()
    {
        return filename;
    }
    
    /**
     * Getter.
     * 
     * @return the line number that this observation was read from.
     */
    @Transient
    public int getLineNumber()
    {
        return lineNumber;
    }
}

package com.srchaven.siwa.model;

/**
 *
 */
//TODO: JavaDoc
public class ErrorObservation extends AbstractObservation
{
    private String errorDescription;
    
    public ErrorObservation(String filename, int lineNumber, String errorDescription)
    {
        super(filename, lineNumber);
        
        if (errorDescription == null)
        {
            throw new IllegalArgumentException("Error description cannot be null");
        }
        
        this.errorDescription = errorDescription;
    }
    
    public ErrorObservation(AbstractObservation sourceObs, String errorDescription)
    {
        super(sourceObs);
        
        if (errorDescription == null)
        {
            throw new IllegalArgumentException("Error description cannot be null");
        }
        
        this.errorDescription = errorDescription;
    }
    
    public String getErrorDescription()
    {
        return errorDescription;
    }
}

package com.srchaven.siwa.transformers;

import com.srchaven.siwa.model.ErrorObservation;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;
import com.srchaven.siwa.model.RawObservation;
import com.srchaven.siwa.util.MessageProxy;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.integration.annotation.Headers;

//TODO: JavaDocs
public class RawObservationToObservationTransformer
{
    private MessageProxy errorMsgProxy;
    
    private boolean outputErrorDetails = false;

    /**
     * Setter.
     * 
     * @param errorMsgProxy the {@code MessageProxy} that will handle error messages. Cannot be {@code null}.
     * 
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void setErrorMsgProxy(MessageProxy errorMsgProxy)
    {
        if (errorMsgProxy == null)
        {
            throw new IllegalArgumentException("Error message proxy cannot be null");
        }
        
        this.errorMsgProxy = errorMsgProxy;
    }
    
    /**
     * Setter.
     * 
     * @param outputErrorDetails flag that indicates if error messages should have error details included.
     */
    public void setOutputErrorDetails(boolean outputErrorDetails)
    {
        this.outputErrorDetails = outputErrorDetails;
    }

    @Transformer
    public List<Observation> transform(@Headers Map<String, Object> headers, List<RawObservation> rawObservationList)
    {
        List<Observation> obsList = new ArrayList<Observation>(rawObservationList.size());
        List<ErrorObservation> badObsList = null;
        for (RawObservation currRawObs : rawObservationList)
        {
            try
            {
                obsList.add(transform(currRawObs));
            }
            catch (Exception error)
            {
                ErrorObservation errObs;
                if (outputErrorDetails)
                {
                    StringBuilder builder = new StringBuilder();
                    
                    builder.append("Exception while converting raw observation to observation\n");
                    
                    builder.append("Message: ").append(error.getMessage()).append("\n");
                    
                    builder.append("Stack trace:\n");
                    StackTraceElement[] stackTrace = error.getStackTrace();
                    for (StackTraceElement currElement : stackTrace)
                    {
                        builder.append(currElement);
                    }
                    
                    errObs = new ErrorObservation(currRawObs, builder.toString());
                }
                else
                {
                    errObs = new ErrorObservation(currRawObs, "");
                }
                
                if (badObsList == null)
                {
                    badObsList = new LinkedList<ErrorObservation>();
                }
            }
        }
        if (badObsList != null)
        {
            errorMsgProxy.sendMessage(headers, badObsList);
        }
        return obsList;
    }
    
    @Transformer
    public Observation transform(RawObservation rawObs)
    {
        return new Observation(rawObs.getFilename(), rawObs.getLineNumber(), rawObs.getObservationFieldVals(), 'C');
    }
}

package com.srchaven.siwa.services;

import com.srchaven.siwa.model.ErrorObservation;
import com.srchaven.siwa.model.RawObservation;
import com.srchaven.siwa.util.MessageProxy;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.integration.annotation.Headers;
import org.springframework.integration.annotation.ServiceActivator;

/**
 *
 */
//TODO: JavaDocs
public class PreBatchingValidator
{
    private final int NUM_FIELDS = 37;

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

    @ServiceActivator
    public List<RawObservation> validate(@Headers Map<String, Object> headerMap, List<RawObservation> obsList)
    {
        List<ErrorObservation> badObsList = null;

        Iterator<RawObservation> iter = obsList.iterator();

        while (iter.hasNext())
        {
            RawObservation currObs = iter.next();

//TODO: Consider changing the validation operation into a List of "Validation" objects, each with a validate(...) method and a produceErrorObservation(...) method. An abstract base class could handle the if(outputErrorDetails) operation.
            if (currObs.getObservationFieldVals().size() < NUM_FIELDS)
            {
                iter.remove();

                ErrorObservation errObs;
                if (outputErrorDetails)
                {
                    errObs = new ErrorObservation(currObs, "Number of fields expected to be " + NUM_FIELDS
                            + " but was actually " + currObs.getFilename());
                }
                else
                {
                    errObs = new ErrorObservation(currObs, "");
                }
                
                if (badObsList == null)
                {
                    badObsList = new LinkedList<ErrorObservation>();
                }
            }
        }

        if (badObsList != null)
        {
            errorMsgProxy.sendMessage(headerMap, badObsList);
        }

        if (obsList.isEmpty())
        {
//TODO: I'm pretty sure this is correct, but we should double-check that a null return is removed rather than being passed on or causing an error
            return null;
        }
        else
        {
            return obsList;
        }
    }
}

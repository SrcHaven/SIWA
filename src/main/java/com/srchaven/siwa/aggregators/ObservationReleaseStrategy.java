package com.srchaven.siwa.aggregators;

import com.srchaven.siwa.model.AbstractObservation;
import com.srchaven.siwa.model.ObservationHeaders;
import java.util.List;
import org.springframework.integration.Message;

/**
 *
 */
//TODO: JavaDocs
public class ObservationReleaseStrategy
{
    public boolean canRelease(List<Message<? extends AbstractObservation>> msgList)
    {
        if (msgList == null)
        {
            throw new IllegalArgumentException("Message list cannot be null");
        }
        
        if (msgList.isEmpty())
        {
            return false;
        }
        
        Object obsFileSizeAsObj = msgList.get(0).getHeaders().get(ObservationHeaders.NUM_OBSERVATIONS);
        if (!(obsFileSizeAsObj instanceof Integer))
        {
            throw new IllegalArgumentException("Value in header field \"" + ObservationHeaders.NUM_OBSERVATIONS
                    + "\" was of type \"" + obsFileSizeAsObj.getClass().getCanonicalName() + "\" expected type \""
                    + Integer.class.getCanonicalName() + "\"");
        }
        Integer obsFileSize = (Integer)obsFileSizeAsObj;
        
        return (msgList.size() >= obsFileSize.intValue());
    }
}

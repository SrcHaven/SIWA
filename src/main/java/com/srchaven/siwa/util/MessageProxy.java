package com.srchaven.siwa.util;

import java.util.Map;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

/**
 *
 */
//TODO: JavaDocs
public class MessageProxy
{
    private MessageChannel destination;
    
    public void sendMessage(Map <String, Object> headers, Object payload)
    {
        destination.send(MessageBuilder.withPayload(payload).copyHeaders(headers).build());
    }

    /**
     * Setter.
     * 
     * @param destination the destination for messages. Cannot be {@code null}.
     * 
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void setDestination(MessageChannel destination)
    {
        if (destination == null)
        {
            throw new IllegalArgumentException("Destination cannot be null");
        }
        
        this.destination = destination;
    }
    
    
}

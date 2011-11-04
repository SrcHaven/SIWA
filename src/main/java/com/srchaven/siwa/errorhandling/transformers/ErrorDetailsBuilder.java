package com.srchaven.siwa.errorhandling.transformers;

import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.message.ErrorMessage;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;

/**
 * Transformer that creates an {@code ErrorDetails} object from an {@code ErrorMessage} object.
 */
public class ErrorDetailsBuilder
{
    /**
     * Creates an {@code ErrorDetails} object from an {@code ErrorMessage} object.
     *
     * @param errMsg the error message.
     *
     * @return the new {@code ErrorDetails} object, wrapped in a {@code Message} with a copy of the original headers.
     *
     * @throws IllegalArgumentException if the payload of {@code errMsg} is not of type {@code MessagingException}
     */
    @Transformer
    public Message<ErrorDetails> buildErrorDetailsFromErrorMessage(ErrorMessage errMsg)
    {
        ErrorDetails details = new ErrorDetails(errMsg);
        Message<ErrorDetails> msg = MessageBuilder.withPayload(details).copyHeaders(errMsg.getHeaders()).build();
        return msg;
    }
}

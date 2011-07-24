package com.srchaven.siwa.errorhandling.transformers;

import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.message.ErrorMessage;

import com.srchaven.siwa.model.ErrorDetails;


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
	 * @return the new {@code ErrorDetails} object.
	 * 
	 * @throws IllegalArgumentException if the payload of {@code errMsg} is not of type {@code MessagingException}
	 */
	@Transformer
	public ErrorDetails buildErrorDetailsFromErrorMessage(ErrorMessage errMsg)
	{
		return new ErrorDetails(errMsg);
	}
}

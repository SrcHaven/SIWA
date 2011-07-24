package com.srchaven.siwa.model;

import java.util.Properties;

import org.springframework.integration.Message;
import org.springframework.integration.MessagingException;
import org.springframework.integration.history.MessageHistory;
import org.springframework.integration.message.ErrorMessage;

/**
 * Data structure used to prepare an error message.
 */
public class ErrorDetails
{
	private final String DETAILS_START = "**************************ERROR DETAILS START**************************"; 
	private final String SEPARATOR = "------------------------"; 
	private final String DETAILS_STOP = "**************************ERROR DETAILS STOP**************************"; 
	
	/**
	 * Constructor.
	 * 
	 * @param errMsg the {@code ErrorMessage} received by an error channel. Must contain a {@code MessagingException}
	 * 		as its payload. Cannot be {@code null}.
	 * 
	 * @throws IllegalArgumentException if the parameter is invalid.
	 */
	public ErrorDetails(ErrorMessage errMsg)
	{
		if (errMsg == null)
		{
			throw new IllegalArgumentException("Error message cannot be null");
		}
		
		Object msgPayload = errMsg.getPayload();
		
		if (!(msgPayload instanceof MessagingException))
		{
			throw new IllegalArgumentException("Error payload must be of type MessagingException");
		}
		
		MessagingException msgException = (MessagingException)msgPayload;
		
		sourceException = msgException.getCause();
		
		failedMessage = msgException.getFailedMessage();
		
		errorDescr = new StringBuilder();

		if (sourceException != null)
		{
			appendSourceExceptionClassName();
			appendSeparator();
			appendErrorCause();
			appendSeparator();
			appendStackTrace();
			appendSeparator();
		}
		
		appendMessageHistory();
		
		appendSeparator();

		appendMessagePayload();
	}
	
	private void appendSeparator()
	{
		errorDescr.append(SEPARATOR);
	}
	
	private void appendSectionHeader(String headerName)
	{
		errorDescr.append("---");
		errorDescr.append(headerName);
		errorDescr.append("---\n");
	}

	private void appendSourceExceptionClassName()
	{
		appendSectionHeader("Source exception");
		errorDescr.append(sourceException.getClass().getName());
		errorDescr.append("\n\n");
	}
	
	private void appendErrorCause()
	{
		String errorCause = sourceException.getMessage();
		if (errorCause != null && !errorCause.isEmpty())
		{
			appendSectionHeader("Exception cause");
			errorDescr.append(errorCause);
			errorDescr.append("\n\n");
		}
	}
	
	private void appendStackTrace()
	{
		appendSectionHeader("Stack trace");
		StackTraceElement[] stackTrace = sourceException.getStackTrace();
		for (StackTraceElement currElement : stackTrace)
		{
			errorDescr.append(currElement);
		}
		errorDescr.append("\n\n");
	}
	
	private void appendMessageHistory()
	{
		MessageHistory history = failedMessage.getHeaders().get(MessageHistory.HEADER_NAME, MessageHistory.class);
		
		if (history != null)
		{
			appendSectionHeader("Message History");
			
			for(Properties currProps : history)
			{
				errorDescr.append(currProps.toString());
				errorDescr.append("\n");
			}
			
			errorDescr.append("\n\n");
		}
	}
	
	private void appendMessagePayload()
	{
		appendSectionHeader("Message payload");
		
		Object payload = failedMessage.getPayload();
		
		if (payload == null)
		{
			errorDescr.append("Null payload");
			
			return;
		}
		
		errorDescr.append("Payload type: ");
		errorDescr.append(payload.getClass().getName());
		errorDescr.append("Payload toString:\n");
		errorDescr.append(payload);

		errorDescr.append("\n\n");
	}
	
	/** The {@code Message} that was being processed when the error occurred */
	private final Message<?> failedMessage;
	
	/** The {@code Throwable} that caused this failure */
	private final Throwable sourceException;
	
	/** The description of the error, which will be outputted to the error description file */
	private final StringBuilder errorDescr;
	
	/**
	 * Getter.
	 * 
	 * @return the {@code Message} that was being processed when the error occurred.
	 */
	public Message<?> getFailedMessage()
	{
		return failedMessage;
	}
	
	/**
	 * Getter.
	 * 
	 * @return the {@code Throwable} that caused this failure
	 */
	public Throwable getSourceException()
	{
		return sourceException;
	}
	
	/**
	 * Adds additional details onto the error description
	 * @param details
	 */
	public void addErrorAdditionalErrorDetails(String details)
	{
		synchronized (errorDescr)
		{
			appendSeparator();
			errorDescr.append(details);
		}
	}
	
	/**
	 * Creates the final error description. This will be the details start string, followed by default error details
	 * (plus any additional details that have been added) separated by the separator string, followed by the deatils
	 * stop string.
	 * 
	 * @return the error description.
	 */
	public String generateErrorDescription()
	{
		synchronized (errorDescr)
		{
			StringBuilder builder = new StringBuilder();
			
			builder.append(DETAILS_START);
			builder.append(errorDescr);
			builder.append(DETAILS_STOP);
			
			return builder.toString();
		}
	}
}

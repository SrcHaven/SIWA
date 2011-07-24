package com.srchaven.siwa.transformers;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;


// TODO: Bean validation
public class SourceFileErrorHandler
{
	/** The logger for this class */
	private static final Logger LOGGER = Logger.getLogger(SourceFileErrorHandler.class);

	/** Reference to the processing directory */
	private File processingDirectory;
	
	/** Reference to the error logging directory */
	private File errorDirectory;

	/**
	 * Setter.
	 * 
	 * @param _path the absolute path to the processing directory.
	 */
	public void setProcessingDirectory(String _path)
	{
		processingDirectory = new File(_path);
	}

	/**
	 * Setter.
	 * 
	 * @param _path the absolute path to the error directory.
	 */
	public void setErrorDirectory(String _path)
	{
		errorDirectory = new File(_path);
	}

	@Transformer
	public List<Observation> receive(List<Observation> records)
	{

		// get the filename where the incomplete set of records originated
		String filename = records.get(0).getFilename();
		LOGGER.trace("Received incomplete set of records that originated in " + filename + ".");

		// move file with problems to error directory
		File fileInProcessingDirectory = new File(processingDirectory, filename);
		File fileInErrorDirectory = new File(errorDirectory, filename);
		fileInProcessingDirectory.renameTo(fileInErrorDirectory);
		// TODO error handling for failure
		
		LOGGER.info("Moved file with errors from " + fileInProcessingDirectory.getPath() + " to "
				+ fileInErrorDirectory.getPath());

		return records;
	}

}

package com.srchaven.siwa.splitters;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Splitter;

import com.srchaven.siwa.model.Observation;


/**
 * Splitter that breaks an input file into multiple {@code Observation} objects.
 */
public class FileToObservationSplitter
{
	/** Logger for this class */
	private static final Logger LOGGER = Logger.getLogger(FileToObservationSplitter.class);

	/**
	 * Splitter method. Breaks an input file into multiple {@code Observation} objects.
	 * 
	 * @param file the file to split
	 * 
	 * @return a {@code List} of {@code Observation} objects extracted from the file
	 * 
	 * @throws IOException if there is an error loading the file
	 */
	@Splitter
	public List<Observation> split(File file) throws IOException
	{

		List<Observation> records = new ArrayList<Observation>();

		Scanner scanner = new Scanner(new FileInputStream(file));
		try
		{
			while (scanner.hasNextLine())
			{
				// TODO: No handling for invalid record length
				records.add(new Observation(file.getName(), scanner.nextLine().split("\\s+"), 'C'));
			}
			LOGGER.debug("Split " + file.getName() + " into " + records.size() + " records.");
		}
		finally
		{
			try
			{
				scanner.close();
			}
			catch (Exception error)
			{
				LOGGER.error("Error closing input stream", error);
			}
		}
		
		return records;
	}
}

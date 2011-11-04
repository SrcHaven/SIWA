package com.srchaven.siwa.splitters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;

import com.srchaven.siwa.model.RawObservation;

/**
 * Splitter that breaks an input file into multiple {@code RawObservation} objects.
 */
public class FileContentsToRawObservationSplitter
{
    /** Logger for this class */
    private static final Logger LOGGER = Logger.getLogger(FileContentsToRawObservationSplitter.class);

    /**
     * Splitter method. Breaks the contents of an input file into multiple {@code Observation} objects.
     *
     * @param file the file to split
     *
     * @return a {@code List} of {@code Observation} objects extracted from the file
     *
     * @throws IOException if there is an error loading the file
     */
    @Splitter
    public List<Message<RawObservation>> split(Message<String> message) throws IOException
    {
        String fileContents = message.getPayload();
        String filename = (String) message.getHeaders().get(FileHeaders.FILENAME);

        List<Message<RawObservation>> records = new ArrayList<Message<RawObservation>>();

        BufferedReader reader = new BufferedReader(new StringReader(fileContents));

        String currLine = null;
        while ((currLine = reader.readLine()) != null)
        {
            RawObservation payload = new RawObservation(currLine, filename);
            Message<RawObservation> msg = MessageBuilder.withPayload(payload).build();
            records.add(msg);
        }

        return records;
    }
}

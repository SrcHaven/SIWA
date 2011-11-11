package com.srchaven.siwa.transformers;

import com.srchaven.siwa.model.ObservationHeaders;
import com.srchaven.siwa.model.RawObservation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.Headers;
import org.springframework.integration.annotation.Payload;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.file.FileHeaders;
import org.springframework.integration.support.MessageBuilder;

/**
 *
 */
//TODO: JavaDoc
public class StringToRawObservationListTransformer
{
    @Transformer
    public Message<List<RawObservation>> transform(@Headers Map<String, Object> headers, @Payload String source)
            throws IOException
    {
        String filename = validateAndCastFilename(headers.get(FileHeaders.FILENAME));
        
        List<RawObservation> rawObsList = new LinkedList<RawObservation>();

        StringReader stringReader = new StringReader(source);
        BufferedReader reader = new BufferedReader(stringReader);

        int currLineNum = 0;

        while (reader.ready())
        {
            String currLine = reader.readLine();
            ++currLineNum;

            String[] fieldsArray = source.split("\\s+");
//TODO: Will Arrays.asList work on an empty array? If so, catch zero-length arrays in the validator. Otherwise, send off to the error channel if the array is empty.
            List<String> fieldsList = Arrays.asList(fieldsArray);

            rawObsList.add(new RawObservation(filename, currLineNum, fieldsList));
        }
        
        Message<List<RawObservation>> msg = MessageBuilder.withPayload(rawObsList).copyHeaders(headers).setHeader(
                ObservationHeaders.NUM_OBSERVATIONS, rawObsList.size()).build();

        return msg;
    }
    
    private String validateAndCastFilename(Object filenameAsObj)
    {
        if (filenameAsObj == null)
        {
            throw new IllegalArgumentException("Message did not have filename in the header field \"" 
                    + FileHeaders.FILENAME + "\"");
        }
        else if (!(filenameAsObj instanceof String))
        {
            throw new IllegalArgumentException("Object in header field \"" + FileHeaders.FILENAME + "\" was of type \""
                    + filenameAsObj.getClass().getCanonicalName() + "\" expected type was \""
                    + String.class.getCanonicalName() + "\"");
        }
        
        String filename = (String)filenameAsObj;
        
        if (filename.isEmpty())
        {
            throw new IllegalArgumentException("Header field \"" + FileHeaders.FILENAME
                    + "\" was empty. Expected a filename.");
        }
        
        return filename;
    }
}

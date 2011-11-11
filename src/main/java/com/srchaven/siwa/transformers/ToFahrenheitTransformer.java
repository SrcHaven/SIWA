package com.srchaven.siwa.transformers;

import static com.srchaven.siwa.util.TemperatureUtils.toFahrenheit;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;
import java.util.List;

/**
 * Transformer that converts a observation's temperature from Celsius to Fahrenheit.
 */
//TODO: This is overkill now. Simplify it. Perhaps simply have a hard min/max cap?
public class ToFahrenheitTransformer
{
    /** The logger for this class */
    private static final Logger LOGGER = Logger.getLogger(ToFahrenheitTransformer.class);

    /** Max string length of result */
    private static final int MAX_LENGTH = 7;
    
    /**
     * Convert a batch of observations' temperature from Celsius to Fahrenheit.
     * 
     * @param observationList the list of observations to convert.
     * 
     * @return the converted observations.
     */
    @Transformer
    public List<Observation> transform(List<Observation> observationList)
    {
        for (Observation currObs : observationList)
        {
            transform(currObs);
        }
        
        return observationList;
    }

    /**
     * Converts a observation's temperature from Celsius to Fahrenheit.
     *
     * @param observation the observation to convert.
     *
     * @return the converted observation.
     */
    @Transformer
    public Observation transform(Observation observation)
    {
        LOGGER.trace("Observation before transformation to Fahrenheit: " + observation);
        // fields that store temperature data: tCalc tHrAvg tMax tMin
        observation.settCalc(safelyConvertToFahrenheitString(observation.gettCalc()));
        observation.settHrAvg(safelyConvertToFahrenheitString(observation.gettHrAvg()));
        observation.settMax(safelyConvertToFahrenheitString(observation.gettMax()));
        observation.settMin(safelyConvertToFahrenheitString(observation.gettMin()));
        LOGGER.trace("Observation after transformation to Fahrenheit:  " + observation);
        return observation;
    }

    /**
     * Converts the string representation of a Celcius temperature measurement into its Fahrenheit equivalent (capped at
     * {@code MAX_LENGTH} characters). If the Celsius value is {@code null} or can't be transformed into a double the
     * result will be the String "Invalid".
     *
     * @param value the string representation of a Celcius temperature measurement
     *
     * @return the temperature measurement converted into Fahrenheit
     */
    private String safelyConvertToFahrenheitString(String value)
    {
        if (value == null)
        {
            return "Invalid";
        }

        double valueAsDouble;
        try
        {
            valueAsDouble = Double.parseDouble(value);
        } catch (NumberFormatException error)
        {
            return "Invalid";
        }

        return toFahrenheit(valueAsDouble, MAX_LENGTH);
    }
}

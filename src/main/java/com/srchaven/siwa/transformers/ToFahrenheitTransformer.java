package com.srchaven.siwa.transformers;

import static com.srchaven.siwa.util.TemperatureUtils.toFahrenheit;

import org.apache.log4j.Logger;
import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;

/**
 * Transformer that converts a record's temperature from Celsius to Fahrenheit.
 */
//TODO: This is overkill now. Simplify it. Perhaps simply have a hard min/max cap?
public class ToFahrenheitTransformer
{
    /** The logger for this class */
    private static final Logger LOGGER = Logger.getLogger(ToFahrenheitTransformer.class);

    /** Max string length of result */
    private static final int MAX_LENGTH = 7;

    /**
     * Converts a record's temperature from Celsius to Fahrenheit.
     *
     * @param record the record to convert.
     *
     * @return the converted record.
     */
    @Transformer
    public Observation transform(Observation record)
    {
        LOGGER.trace("Message before transformation to Fahrenheit: " + record);
        // fields that store temperature data: tCalc tHrAvg tMax tMin
        record.settCalc(safelyConvertToFahrenheitString(record.gettCalc()));
        record.settHrAvg(safelyConvertToFahrenheitString(record.gettHrAvg()));
        record.settMax(safelyConvertToFahrenheitString(record.gettMax()));
        record.settMin(safelyConvertToFahrenheitString(record.gettMin()));
        LOGGER.trace("Message after transformation to Fahrenheit:  " + record);
        return record;
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

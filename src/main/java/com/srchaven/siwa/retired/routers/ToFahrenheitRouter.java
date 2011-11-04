package com.srchaven.siwa.retired.routers;

import org.springframework.integration.annotation.Router;

import com.srchaven.siwa.model.Observation;

/**
 * Router that sends all records on to the {@code toFahrenheitTransformerChannel} channel to convert measurements to
 * Fahrenheit EXCEPT those that have an invalid temperature reading, which go on to the
 * {@code toFahrenheitBypassChannel}.
 */
public class ToFahrenheitRouter
{
    @Router
    public String route(Observation record)
    {
        // fields that store temperature data: tCalc tHrAvg tMax tMin
        if (record.gettCalc() != null || record.gettHrAvg() != null || record.gettMax() != null
                || record.gettMin() != null)
        {
            return "toFahrenheitTransformerChannel";
        }
        else
        {
            return "toFahrenheitBypassChannel";
        }
    }
}

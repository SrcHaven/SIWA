package com.srchaven.siwa.transformers;

import org.springframework.integration.annotation.Transformer;

import com.srchaven.siwa.model.Observation;
import com.srchaven.siwa.model.RawObservation;

//TODO: JavaDocs
public class RawObservationToObservationTransformer
{
    @Transformer
    public Observation transform(RawObservation rawObs)
    {
        String sourceString = rawObs.getSourceString();
        return new Observation(sourceString, rawObs.getFilename(), sourceString.split("\\s+"), 'C');
    }
}

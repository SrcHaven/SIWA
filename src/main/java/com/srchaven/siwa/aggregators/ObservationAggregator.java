package com.srchaven.siwa.aggregators;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import com.srchaven.siwa.model.AbstractObservation;
import com.srchaven.siwa.model.AggregatedObservations;
import com.srchaven.siwa.model.ErrorObservation;
import com.srchaven.siwa.model.Observation;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.integration.Message;
import org.springframework.integration.annotation.Aggregator;

/**
 *
 */
public class ObservationAggregator
{
    private static final Logger LOGGER = Logger.getLogger(ObservationAggregator.class);

    @Aggregator
    public AggregatedObservations aggregate(List<Message<? extends AbstractObservation>> messages)
    {
        List<Observation> goodObservations = new LinkedList<Observation>();
        List<ErrorObservation> badObservations = new LinkedList<ErrorObservation>();

//TODO: We really should make sure the filenames match on ALL the messages--right now we just grab the first one that comes along
        String filename = null;

        AbstractObservation currPayload = null;
        for (Message<? extends AbstractObservation> msg : messages)
        {
            currPayload = msg.getPayload();

            if (currPayload instanceof Observation)
            {
                Observation currObs = (Observation) currPayload;
                goodObservations.add(currObs);
            }
            else if (currPayload instanceof ErrorObservation)
            {
                badObservations.add((ErrorObservation)currPayload);
            }
            else
            {
                //TODO: This is a bad solution. We need to figure out what to do here.
                LOGGER.fatal("Aggregator received message of unsupported type \"" + currPayload.getClass().getName()
                        + "\"");
                System.exit(1);
            }
        }
        
        //The safety check shouldn't be necessary, but I'm leaving it in just in case
        filename = currPayload == null ? null : currPayload.getFilename();

        return new AggregatedObservations(goodObservations, badObservations, filename);
    }
}

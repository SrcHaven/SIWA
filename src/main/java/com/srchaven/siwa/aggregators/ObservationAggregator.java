package com.srchaven.siwa.aggregators;

import com.srchaven.siwa.errorhandling.model.ErrorDetails;
import com.srchaven.siwa.model.AggregatedObservations;
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
    public AggregatedObservations aggregate(List<Message<?>> messages)
    {
        List<Observation> goodObservations = new LinkedList<Observation>();
        List<ErrorDetails> badObservations = new LinkedList<ErrorDetails>();

//TODO: We really should make sure the filenames match on ALL the messages--right now we just grab the first one that comes along
        String filename = null;

        for (Message msg : messages)
        {
            Object payload = msg.getPayload();

            if (payload instanceof Observation)
            {
                Observation currObs = (Observation) payload;
                goodObservations.add(currObs);

                if (filename == null)
                {
                    filename = currObs.getFilename();
                }
            }
            else if (payload instanceof ErrorDetails)
            {
                ErrorDetails errDet = (ErrorDetails) payload;

                if (!(errDet.getFailedMessage().getPayload() instanceof Observation))
                {
                    LOGGER.error("Error description with payload of type \""
                            + errDet.getFailedMessage().getClass().getName()
                            + "\" received in aggregator. Only supported failed message type is Observation."
                            + " Invalid message was discarded.");
                }
                else
                {
                    badObservations.add(errDet);

                    if (filename == null)
                    {
                        filename = ((Observation) errDet.getFailedMessage()).getFilename();
                    }
                }
            }
            else
            {
                //TODO: This is a bad solution. We need to figure out what to do here.
                LOGGER.fatal("Aggregator received message of unsupported type \"" + payload.getClass().getName()
                        + "\"");
                System.exit(1);
            }
        }

        return new AggregatedObservations(goodObservations, badObservations, filename);
    }
}

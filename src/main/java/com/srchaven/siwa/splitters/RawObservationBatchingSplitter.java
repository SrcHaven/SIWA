package com.srchaven.siwa.splitters;

import com.srchaven.siwa.model.RawObservation;
import java.util.ArrayList;
import java.util.List;
import org.springframework.integration.annotation.Splitter;

/**
 *
 */
//TODO: JavaDocs
public class RawObservationBatchingSplitter
{
    private int batchSize = 200;
    
    @Splitter
    public List<List<RawObservation>> createBatches(List<RawObservation> sourceObsList)
    {
        final int partialBatchSize = sourceObsList.size() % batchSize;
        final int numBatches = (sourceObsList.size() / batchSize) + (partialBatchSize > 0 ? 1 : 0);
//TODO: Is it more efficient to have a partial batch, or a big "last batch"?

        List<List<RawObservation>> batchesList = new ArrayList(numBatches);
        
        List<RawObservation> currBatch;
        //If the only batch is less that the batch size
        if ((numBatches == 1) && (partialBatchSize > 0))
        {
            currBatch = new ArrayList(partialBatchSize);
        }
        else
        {
            currBatch = new ArrayList(batchSize);
        }
        
        int currBatchNumber = 1;
        for(RawObservation currObs: sourceObsList)
        {
            currBatch.add(currObs);
            
            if (currBatch.size() >= batchSize)
            {
                batchesList.add(currBatch);
                ++currBatchNumber;
                //If we've just stored the last observation
                if (currBatchNumber > numBatches)
                {
                    break;
                }
                //If we're on the last batch and it's a partial one
                else if (currBatchNumber == numBatches && partialBatchSize > 0)
                {
                    currBatch = new ArrayList(partialBatchSize);
                }
                else
                {
                    currBatch = new ArrayList(batchSize);
                }
            }
        }
        
        return batchesList;
    }

    /**
     * Setter.
     * 
     * @param batchSize the size of observation batches. Cannot be less than one.
     * 
     * @throws IllegalArgumentException if the parameter is invalid.
     */
    public void setBatchSize(int batchSize)
    {
        if (batchSize < 1)
        {
            throw new IllegalArgumentException("Batch size cannot be less than one");
        }
        
        this.batchSize = batchSize;
    }
    
    
}

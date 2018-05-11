package com.diff.images.Adapters;

import com.diff.images.Contracts.AlgorithmsContract;
import com.diff.images.Models.ClustersModel;

import java.io.IOException;
import java.util.concurrent.Callable;

public class AlgorithmsAdapter{

    AlgorithmsContract algorithm;

    public AlgorithmsAdapter(AlgorithmsContract algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * Execute algorithm logic
     */
    public void executeAlgorithm() throws IOException {
        algorithm.executeAlgorithm();
    }

    /**
     * Get algorithm output
     *
     * @return
     */
    public <T> T getOutput() {
        return algorithm.getOutput();
    }

    public void printAlgorithmData() {
        algorithm.printAlgorithmData();
    }

    /**
     * Get execution time of algorithm in milliseconds
     */
    public long getAlgorithmTime() {
        return algorithm.getAlgorithmTime();
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */

}

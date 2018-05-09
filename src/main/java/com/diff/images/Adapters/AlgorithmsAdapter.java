package com.diff.images.Adapters;

import com.diff.images.Contracts.AlgorithmsContract;

import java.io.IOException;

public class AlgorithmsAdapter implements AlgorithmsContract{

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
}

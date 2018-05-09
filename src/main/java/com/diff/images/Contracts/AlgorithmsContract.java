package com.diff.images.Contracts;

import java.io.IOException;

public interface AlgorithmsContract {

    /**
     * Execute algorithm logic
     */
    public void executeAlgorithm() throws IOException;

    /**
     * Get algorithm output
     * @param <T>
     * @return
     */
    public <T> T getOutput();


}

package com.diff.images.Algorithms;

import com.diff.images.Contracts.AlgorithmsContract;

import java.io.IOException;

public class JaccardSimilarityAlgorithm implements AlgorithmsContract {

    private final int biggerSetLength;
    /**
     * Execute algorithm logic
     */

    private int[] firstBigSet;
    private int[] secondBigSet;

    float[] jaccardSimilarity = new float[1];

    public JaccardSimilarityAlgorithm(int[] firstBigSet, int[] secondBigSet) {
        this.firstBigSet = firstBigSet;
        this.secondBigSet = secondBigSet;
        this.biggerSetLength = firstBigSet.length > secondBigSet.length? firstBigSet.length : secondBigSet.length;
    }

    public void executeAlgorithm() throws IOException {
        float similarity = this.findIntersection() / ((firstBigSet.length + secondBigSet.length) - this.findIntersection());
        this.jaccardSimilarity[0] = similarity;

    }

    /**
     * Get algorithm output
     *
     * @return
     */
    public float[] getOutput() {
        return this.jaccardSimilarity;
    }

    private int findIntersection(){

        int intersection = 0;
        int startSize = firstBigSet.length;

        if(firstBigSet.length == biggerSetLength){
            startSize = secondBigSet.length;
        }

        for(int i = 0; i < startSize; i++){
            if(firstBigSet[i] == secondBigSet[i]){
                    intersection ++;
            }
        }

        return intersection;
    }
}

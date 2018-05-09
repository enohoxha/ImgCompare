package com.diff.images.Algorithms;

import com.diff.images.Contracts.AlgorithmsContract;
import com.diff.images.Models.ClustersModel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JaccardSimilarityAlgorithm implements AlgorithmsContract {

    private final int biggerSetLength;
    /**
     * Execute algorithm logic
     */

    private ClustersModel[] firstBigSet;
    private ClustersModel[] secondBigSet;
    private ClustersModel[] intersectionArray;

    float[] jaccardSimilarity = new float[1];

    public JaccardSimilarityAlgorithm(ClustersModel[] firstBigSet, ClustersModel[] secondBigSet) {
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

        List <Integer> intersectionArray = new LinkedList<Integer>();

        for(int i = 0; i < firstBigSet.length; i++ ){
            if (!intersectionArray.contains(firstBigSet[i].getRGB())) {
                if (existInArray(secondBigSet, firstBigSet[i].getRGB())) {
                    intersectionArray.add(firstBigSet[i].getRGB());
                }
            }
        }

        return intersectionArray.size();
    }

    private boolean existInArray(ClustersModel[] array, int element){

        boolean exist = false;

        for(int i = 0; i < array.length; i++ ){
            if(array[i].getRGB() == element){
                exist = true;
                break;
            }
        }
        return exist;
    }


}

package com.diff.images.Algorithms;

import com.diff.images.Config.Config;
import com.diff.images.Contracts.AlgorithmsContract;
import com.diff.images.Models.ClustersModel;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JaccardSimilarityAlgorithm implements AlgorithmsContract {

    /**
     * Execute algorithm logic
     */

    private ClustersModel[] firstBigSet;
    private ClustersModel[] secondBigSet;
    private long start;
    private long end;

    double[] jaccardSimilarity = new double[1];

    public JaccardSimilarityAlgorithm(ClustersModel[] firstBigSet, ClustersModel[] secondBigSet) {

        this.firstBigSet = firstBigSet;
        this.secondBigSet = secondBigSet;
    }

    public void executeAlgorithm() throws IOException {
        start = System.currentTimeMillis();
        int x = this.findIntersection();
        int y = ((firstBigSet.length + secondBigSet.length) - this.findIntersection());
        double similarity = (double) x / y;
        this.jaccardSimilarity[0] = similarity;
        end = System.currentTimeMillis();
    }

    public void printAlgorithmData() {
        System.out.println("Similarity between this images is : " +  this.jaccardSimilarity[0]);
        System.out.println("Execution Time for Similarity : " + getAlgorithmTime());
        System.out.println("\n");
    }

    /**
     * Get execution time of algorithm in milliseconds
     */
    public long getAlgorithmTime() {
        return end-start;
    }

    /**
     * Get algorithm output
     *
     * @return
     */
    public double[] getOutput() {
        return this.jaccardSimilarity;
    }

    private int findIntersection() throws IOException {

        List <Integer> intersectionArray = new LinkedList<Integer>();

        for(int i = 0; i < firstBigSet.length; i++ ){
            if (!intersectionArray.contains(firstBigSet[i].getRGB())) {
                if (existInArray(secondBigSet, firstBigSet[i])) {
                    intersectionArray.add(firstBigSet[i].getRGB());
                }
            }
        }

        return intersectionArray.size();
    }

    private boolean existInArray(ClustersModel[] array, ClustersModel element) throws IOException {

        boolean exist = false;

        for(int i = 0; i < array.length; i++ ){
            double d = array[i].distance(element.getRGB());
            if(d < Integer.parseInt(Config.getProperty("colour_deviation"))){
                return true;
            }
        }
        return exist;
    }


}

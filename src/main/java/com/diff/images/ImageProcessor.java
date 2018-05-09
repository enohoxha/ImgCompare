package com.diff.images;
import com.diff.images.Adapters.AlgorithmsAdapter;
import com.diff.images.Algorithms.JaccardSimilarityAlgorithm;
import com.diff.images.Algorithms.KMeansAlgorithm;
import com.diff.images.Models.ClustersModel;

import java.io.IOException;

public class ImageProcessor {


    public static void main(String[] args) throws IOException {


        String homeDir = "C:\\Users\\User\\code\\java\\imageprocess\\img\\";
        String src = homeDir+"b1.JPEG";
        String src2 = homeDir+"b5.JPEG";

        int numberOfClusters = 8;

        AlgorithmsAdapter algorithmsAdapter= new AlgorithmsAdapter(new KMeansAlgorithm(numberOfClusters, src));
        algorithmsAdapter.executeAlgorithm();
        ClustersModel[] response = algorithmsAdapter.getOutput();

        AlgorithmsAdapter algorithmsAdapter2= new AlgorithmsAdapter(new KMeansAlgorithm(numberOfClusters, src2));
        algorithmsAdapter2.executeAlgorithm();
        ClustersModel[] response2 = algorithmsAdapter2.getOutput();


        AlgorithmsAdapter algorithmsAdapter3= new AlgorithmsAdapter(new JaccardSimilarityAlgorithm(response, response2));
        algorithmsAdapter3.executeAlgorithm();
        float[] response3 = algorithmsAdapter3.getOutput();

        System.out.println("Sim: " + response3[0]);

    }



}

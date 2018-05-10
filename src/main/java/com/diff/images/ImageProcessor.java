package com.diff.images;
import com.diff.images.Adapters.AlgorithmsAdapter;
import com.diff.images.Algorithms.JaccardSimilarityAlgorithm;
import com.diff.images.Algorithms.KMeansAlgorithm;
import com.diff.images.Config.Config;
import com.diff.images.Database.DbConnection;
import com.diff.images.Models.ClustersModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ImageProcessor {


    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException, SQLException, ClassNotFoundException {

        DbConnection db = new DbConnection();




        ExecutorService service = Executors.newFixedThreadPool(2);



        String homeDir = "/home/eno/code/JavaEE/Imageprocesor/img/";
        String src = homeDir+"b1.jpg";
        String src2 = homeDir+"b4.png";

        int numberOfClusters = Integer.parseInt(Config.getProperty("clusters_number"));

        long start = System.currentTimeMillis();

        AlgorithmsAdapter algorithmsAdapter= new AlgorithmsAdapter(new KMeansAlgorithm(numberOfClusters, src));
        Future<ClustersModel[]> response =  service.submit(algorithmsAdapter);

        AlgorithmsAdapter algorithmsAdapter2= new AlgorithmsAdapter(new KMeansAlgorithm(numberOfClusters, src2));
        Future<ClustersModel[]> response2 =  service.submit(algorithmsAdapter2);



        AlgorithmsAdapter algorithmsAdapter3= new AlgorithmsAdapter(new JaccardSimilarityAlgorithm(response.get(), response2.get()));
        algorithmsAdapter3.executeAlgorithm();
        double[] response3 = algorithmsAdapter3.getOutput();

        long end = System.currentTimeMillis();

/*

        System.out.println("\n**************************************Execution info for first compare**************************************");
        algorithmsAdapter.printAlgorithmData();
        algorithmsAdapter2.printAlgorithmData();

*/
        algorithmsAdapter3.printAlgorithmData();
        System.out.println("\n-----------------------------------------------------End-------------------------------------------------------------\n");
        System.out.println("Total execution time : " + (double)(algorithmsAdapter.getAlgorithmTime() + algorithmsAdapter2.getAlgorithmTime() +
        algorithmsAdapter3.getAlgorithmTime()) / 1000 + " sec");
        System.out.println("Total: " + (double)(end-start) / 1000 + "sec");

    }



}

package com.diff.images.Services;

import com.diff.images.Adapters.AlgorithmsAdapter;
import com.diff.images.Algorithms.JaccardSimilarityAlgorithm;
import com.diff.images.Algorithms.KMeansAlgorithm;
import com.diff.images.Config.Config;
import com.diff.images.Database.DbConnection;
import com.diff.images.Models.ClustersModel;
import com.diff.images.Models.ImageModel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageCompareService extends Thread {

    private Thread t;
    DbConnection db;
    private  ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    int numberOfClusters;
    List<ImageModel> images;
    String name;
    String mode;
    int end;
    HashMap<String, ClustersModel[]> allElements;

    public ImageCompareService(List<ImageModel> images, String name, int end, HashMap allElements,DbConnection db, String mode) throws IOException {
        this.images = images;
        this.name = name;
        numberOfClusters = Integer.parseInt(Config.getProperty("clusters_number"));
        this.db = db;
        this.end = end;
        this.mode = mode;
        this.allElements = allElements;
    }

    public ImageCompareService(List<ImageModel> images, String name, DbConnection db, ObjectOutputStream outputStream,
                                ObjectInputStream objectInputStream, String mode) throws IOException {
        this.images = images;
        this.name = name;
        numberOfClusters = Integer.parseInt(Config.getProperty("clusters_number"));
        this.db = db;
        this.outputStream = outputStream;
        this.inputStream = objectInputStream;
        this.mode = mode;
    }

    public void setType(String mode){
        this.mode = mode;
    }
    public ClustersModel[] getModelForImage(String img) throws IOException {
        String imgLocation = Config.getProperty("image_home_dir") + img;
        AlgorithmsAdapter algorithmsAdapter = new AlgorithmsAdapter(new KMeansAlgorithm(numberOfClusters, imgLocation));
        algorithmsAdapter.executeAlgorithm();
        //algorithmsAdapter.printAlgorithmData();
        return algorithmsAdapter.getOutput();
    }

    public double compareImages(ClustersModel[] m1, ClustersModel[] m2, int i, int j) throws IOException, SQLException {

        AlgorithmsAdapter similarityAlg = new AlgorithmsAdapter(new JaccardSimilarityAlgorithm(m1, m2));
        similarityAlg.executeAlgorithm();
        double[] similarity = similarityAlg.getOutput();
        double sim = similarity[0];
        db.executeQuery("INSERT INTO compare (`image_id`, `image_id_2`, `sim`) VALUES (" + images.get(i).getId() + ", " + images.get(j).getId() + ", " + sim + ")", true);
        //similarityAlg.printAlgorithmData();
        return similarity[0];
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        System.out.println("Starting thread: " + this.getName());

        if(this.mode.equals("live_run")){
            this.liveCompare();
        } else if(this.mode.equals("dry_run")){
            int count = 0;
            double start = System.currentTimeMillis();
            for (int i = 0 ; i < images.size(); i++){
                try {
                    count++;
                    this.getModelForImageToFile(images.get(i).getName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            double endt = System.currentTimeMillis();
            System.out.println("==================================================================================================");
            System.out.println("Thread " + this.getName() + " finished" +
                    "\n" +
                    "Time (sec): " + (endt - start) / 1000 + "\n" +
                    "Time (min): " + ((endt - start) / 1000) / 60 + "\n" +
                            "Count : " + count
                    );
            System.out.println("==================================================================================================");

        }

    }

    private void liveCompare() {
        int compare = 0;
        int noCompare = 0;
        double start = System.currentTimeMillis();
        try {
            for (int i = 0; i < end; i++) {
                if(i < images.size()){
                    ClustersModel[] c1 = allElements.get(images.get(i).getName());

                    for (int j = i+1; j < images.size(); j++) {
                        if (this.db.compare(images.get(i).getId(), images.get(j).getId())){
                            compare++;
                            ClustersModel[] c2 = allElements.get(images.get(j).getName());
                            compareImages(c1, c2, i, j);
                        } else {
                            noCompare++;
                            db.executeQuery("INSERT INTO compare (`image_id`, `image_id_2`, `sim`) VALUES (" + images.get(i).getId() + ", " + images.get(j).getId() + ", " + 1 + ")", true);

                        }

                    }
                }

            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        double endt = System.currentTimeMillis();
        System.out.println("==================================================================================================");
        System.out.println("Thread " + this.getName() + " finnished" +
                "\n" +
                "Time (sec): " + (endt - start) / 1000 + "\n" +
                "Time (min): " + ((endt - start) / 1000) / 60 + "\n" +
                "Comparations: " + compare +"\n" +
                "Not Compared: " + noCompare);
        System.out.println("==================================================================================================");

    }

    public ClustersModel[] getModelForImageToFile(String img) throws IOException {
        String imgLocation = Config.getProperty("image_home_dir") + img;
        AlgorithmsAdapter algorithmsAdapter = new AlgorithmsAdapter(new KMeansAlgorithm(numberOfClusters, imgLocation));
        algorithmsAdapter.executeAlgorithm();

        //algorithmsAdapter.printAlgorithmData();
        ClustersModel[] clustersModels = algorithmsAdapter.getOutput();
        HashMap<String, ClustersModel[]> clusterModelHmap = new HashMap<String, ClustersModel[]>();
        try {
            clusterModelHmap.put(img, clustersModels);
            outputStream.writeObject(clusterModelHmap);
        } catch (Exception e){
            System.out.println(img);
            e.printStackTrace();
        }


        return algorithmsAdapter.getOutput();

    }




}

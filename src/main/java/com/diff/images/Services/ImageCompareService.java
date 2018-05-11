package com.diff.images.Services;

import com.diff.images.Adapters.AlgorithmsAdapter;
import com.diff.images.Algorithms.JaccardSimilarityAlgorithm;
import com.diff.images.Algorithms.KMeansAlgorithm;
import com.diff.images.Config.Config;
import com.diff.images.Database.DbConnection;
import com.diff.images.Models.ClustersModel;
import com.diff.images.Models.ImageModel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageCompareService extends Thread{
    private Thread t;
    DbConnection db;
    int numberOfClusters ;
    List<ImageModel> images;
    String name;
    ImageModel imageModel;
    public ImageCompareService(ImageModel imageModel, List<ImageModel> images, String name, DbConnection db) throws IOException, SQLException, ClassNotFoundException {
        this.images = images;
        this.name = name;
        this.imageModel = imageModel;
        numberOfClusters = Integer.parseInt(Config.getProperty("clusters_number"));
        this.db = db;
    }



    public ClustersModel[] getModelForImage(String img) throws IOException {
        AlgorithmsAdapter algorithmsAdapter= new AlgorithmsAdapter(new KMeansAlgorithm(numberOfClusters, Config.getProperty("image_home_dir")+img));
        algorithmsAdapter.executeAlgorithm();
        algorithmsAdapter.printAlgorithmData();
        return algorithmsAdapter.getOutput();
    }

    public double compareImages(ClustersModel[] m1, ClustersModel[] m2, int j) throws IOException, SQLException {

        AlgorithmsAdapter similarityAlg = new AlgorithmsAdapter(new JaccardSimilarityAlgorithm(m1, m2));
        similarityAlg.executeAlgorithm();
        double[] similarity = similarityAlg.getOutput();
        double sim = similarity[0];
        db.executeQuery("INSERT INTO compare (`image_id`, `image_id_2`, `sim`) VALUES (" + imageModel.getId() + ", " + images.get(j).getId() + ", " + sim +")", true);
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
        //System.out.println("Size " + images.size() + "thread- "+ this.name);
        ClustersModel[] img1 = null;
        try {
            img1 = getModelForImage(imageModel.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < images.size(); i++){
            ClustersModel[] img2 = new ClustersModel[0];
            try {
                img2 = getModelForImage(images.get(i).getName());
                compareImages(img1, img2, i);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

    }

    public void start () {
        if (t == null) {
            t = new Thread (this, name);
            t.start ();
        }
    }
}

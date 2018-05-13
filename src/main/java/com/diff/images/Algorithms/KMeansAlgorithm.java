package com.diff.images.Algorithms;

import com.diff.images.Config.Config;
import com.diff.images.Contracts.AlgorithmsContract;
import com.diff.images.Models.ClustersModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class KMeansAlgorithm implements AlgorithmsContract{

    private int loops;
    private  String imageLocation;
    private BufferedImage image;
    private int numberOfClassifiers;
    private int imageWidth;
    private int imageHeight;

    private int[] bagOfWords;

    private ClustersModel[] clusters;

    private long start;
    private long end;

    /**
     * Get required input to run the algorithm
     *
     * @param numberOfClassifiers
     * @param imgLocation
     * @throws IOException
     */
    public KMeansAlgorithm(int numberOfClassifiers,  String imgLocation) throws IOException {

        this.imageLocation = imgLocation;
        this.image =  ImageIO.read(new File(imgLocation));
        this.numberOfClassifiers = numberOfClassifiers;
        imageWidth = image.getWidth();
        imageHeight = image.getHeight();

    }

    /**
     * Execute algorithm logic
     */
    public void executeAlgorithm() throws IOException {


        // initialize clusters
        clusters = createClusters(image, numberOfClassifiers);

        // initialize bag of words wil -1 values
        setBagOfWords();
        start = System.currentTimeMillis();

        // at first loop all pixels will move their clusters
        boolean pixelChangedCluster = true;

        loops = 0;

        // loop until no changes are made to pixels
        while (pixelChangedCluster) {

            // supposed that cluster base has not been caged
            pixelChangedCluster = false;
            loops++;

            // loop all pixels of image
            for (int y = 0; y < imageHeight; y++) {

                for (int x = 0; x < imageWidth; x++) {

                    //Get pixel colour
                    int pixel = image.getRGB(x, y);

                    ClustersModel cluster = findNearestCluster(pixel);

                    // set cluster id on bag of words
                    if (bagOfWords[imageWidth * y + x] != cluster.getId()) {

                        this.changePixels(cluster, x, y, pixel);

                        // change variable to true for continue looping
                        pixelChangedCluster = true;

                        // update bagOfWords
                        bagOfWords[imageWidth * y + x] = cluster.getId();

                    }
                }

            }
        }
        // create result image
        end = System.currentTimeMillis();
        if(Integer.parseInt(Config.getProperty("print_processed_image")) == 1){
            createRenderedImage();
        }
    }

    /**
     * Print algorithm details for each image
     */
    public void printAlgorithmData() {
        System.out.println("Clustered to " + numberOfClassifiers + " clusters in " + loops
                + " loops.");
        System.out.println("Execution Time for K-Mean : " + getAlgorithmTime());
        System.out.println("\n");
    }

    /**
     * Get execution time of algorithm in milliseconds
     */
    public long getAlgorithmTime() {
        return end-start;
    }

    private void changePixels(ClustersModel cluster, int x, int y, int pixel) {

        // If pixel  has been clustered before
        // Remove it form cluster
        if (bagOfWords[imageWidth * y + x] != -1) {
            clusters[bagOfWords[imageWidth * y + x]].removePixel(pixel);
        }

        // add pixel to the new cluster
        cluster.addPixel(pixel);
    }

    /**
     *  Create clusters and put them in some point at first
     * @param image
     * @param numberOfClassifiers
     * @return
     */
    private ClustersModel[] createClusters(BufferedImage image, int numberOfClassifiers) {

        ClustersModel[] result = new ClustersModel[numberOfClassifiers];
        // (x and y) are k-means starting points
        //@ToDo Maybe randomize starting points
        int x = 0;
        int y = 0;
        int dx = image.getWidth() / numberOfClassifiers;
        int dy = image.getHeight() / numberOfClassifiers;
        // Initialize clusters
        for (int i = 0; i < numberOfClassifiers; i++) {
            result[i] = new ClustersModel(i, image.getRGB(x, y));
            x += dx;
            y += dy;
        }

        return result;
    }

    /**
     * Get minimal value for the nearest colour to cluster them later
     *
     * @param rgb
     * @return
     */
    //ToDo improve search for nearest cluster
    private ClustersModel findNearestCluster(int rgb) {

        //Cluster null at first
        ClustersModel cluster = null;


        //Get a min value (very large)
        int min = Integer.MAX_VALUE;

        for (int i = 0; i < clusters.length; i++) {

            int distance = clusters[i].distance(rgb);

            if (distance < min) {
                min = distance;
                cluster = clusters[i];
            }

        }

        return cluster;
    }

    /**
     * First create an empty bag of words
     */
    private void setBagOfWords() {

        this.bagOfWords = new int[imageWidth * imageHeight];

        Arrays.fill(bagOfWords, -1);

    }

    private void createRenderedImage() throws IOException {
        // create result image
        BufferedImage image = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int clusterId = bagOfWords[imageWidth * y + x];
                image.setRGB(x, y, clusters[clusterId].getRGB());
            }

        }
        File outputfile = new File(imageLocation + "rendered_image.jpg");

        ImageIO.write(image, "jpg", outputfile);
    }

    public ClustersModel[] getOutput() {
        return this.clusters;
    }


}

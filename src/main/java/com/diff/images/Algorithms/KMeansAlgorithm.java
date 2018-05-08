package com.diff.images.Algorithms;

import com.diff.images.Models.ClustersModel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

public class KMeansAlgorithm {

    ClustersModel[] clusters;
    int mode;

    public static final int MODE_CONTINUOUS = 1;
    public static final int MODE_ITERATIVE = 2;

    public KMeansAlgorithm(String m) {
        int mode = 1;
        if (m.equals("-i")) {
            mode = MODE_ITERATIVE;
        } else if (m.equals("-c")) {
            mode = MODE_CONTINUOUS;
        }
    }

    public BufferedImage calculate(BufferedImage image, int numberOfClassifiers) {

        long start = System.currentTimeMillis();
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        //initialize clusters
        clusters = createClusters(image, numberOfClassifiers);

        // create cluster lookup table and initialize with -1
        int[] lookupTable = new int[imageWidth * imageHeight];
        Arrays.fill(lookupTable, -1);
        // at first loop all pixels will move their clusters
        boolean pixelChangedCluster = true;
        // loop until all clusters are stable!
        int loops = 0;

        while (pixelChangedCluster) {
            //supposed that cluster base has not been caged
            pixelChangedCluster = false;
            loops++;
            //loop all pixels of image
            for (int y = 0; y < imageHeight; y++) {

                for (int x = 0; x < imageWidth; x++) {
                    //Get pixel colour
                    int pixel = image.getRGB(x, y);

                    ClustersModel cluster = findMinimalCluster(pixel);

                    if (lookupTable[imageWidth * y + x] != cluster.getId()) {
                        // cluster changed
                        if (mode == MODE_CONTINUOUS) {
                            if (lookupTable[imageWidth * y + x] != -1) {
                                // remove from possible previous
                                // cluster
                                clusters[lookupTable[imageWidth * y + x]].removePixel(pixel);
                            }
                            // add pixel to cluster
                            cluster.addPixel(pixel);
                        }
                        // continue looping
                        pixelChangedCluster = true;

                        // update lookupTable
                        lookupTable[imageWidth * y + x] = cluster.getId();

                    }
                }

            }
            if (mode == MODE_ITERATIVE) {
                // update clusters
                for (int i = 0; i < clusters.length; i++) {
                    clusters[i].clear();
                }
                for (int y = 0; y < imageHeight; y++) {
                    for (int x = 0; x < imageWidth; x++) {
                        int clusterId = lookupTable[imageWidth * y + x];
                        // add pixels to cluster
                        clusters[clusterId].addPixel(
                                image.getRGB(x, y));
                    }
                }
            }

        }
        // create result image
        BufferedImage result = new BufferedImage(imageWidth, imageHeight,
                BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < imageHeight; y++) {
            for (int x = 0; x < imageWidth; x++) {
                int clusterId = lookupTable[imageWidth * y + x];
                result.setRGB(x, y, clusters[clusterId].getRGB());
            }

        }
        long end = System.currentTimeMillis();
        System.out.println("Clustered to " + numberOfClassifiers + " clusters in " + loops
                + " loops in " + (end - start) + " ms.");
        System.out.println("Clusers: \n\n\n");
        for (ClustersModel cluster : clusters) {
            System.out.println(cluster.toString()+"\n\n");
        }

        return result;
    }

    public ClustersModel[] createClusters(BufferedImage image, int numberOfClassifiers) {
        // Here the clusters are taken with specific steps,
        // so the result looks always same with same image.
        // You can randomize the cluster centers, if you like.

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
    //ToDo improve search for minimal cluster
    public ClustersModel findMinimalCluster(int rgb) {

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

    public static void saveImage(String filename,
                                 BufferedImage image) {
        File file = new File(filename);
        try {
            ImageIO.write(image, "jpeg", file);
        } catch (Exception e) {
            System.out.println(e.toString() + " Image '" + filename
                    + "' saving failed.");
        }
    }

    public static BufferedImage loadImage(String filename) {
        BufferedImage result = null;
        try {
            result = ImageIO.read(new File(filename));
        } catch (Exception e) {
            System.out.println(e.toString() + " Image '"
                    + filename + "' not found.");
        }
        return result;
    }
}

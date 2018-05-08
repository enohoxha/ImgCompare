package com.diff.images;
import com.diff.images.Algorithms.KMeansAlgorithm;

import java.awt.image.BufferedImage;

public class ImageProcessor {


    public static void main(String[] args) {
        String homeDir = "/home/eno/code/JavaEE/Imageprocesor/img/";
        String src = homeDir+"b4.jpeg";
        String dst = homeDir+"b3.jpeg";
        int k = 6;
        String m = "-i";

        // create new KMeans object
        KMeansAlgorithm kMeans = new KMeansAlgorithm(m);

        // call the function to actually start the clustering
        BufferedImage dstImage = kMeans.calculate(kMeans.loadImage(src), k);
        // save the resulting image
        kMeans.saveImage(dst, dstImage);

    }



}

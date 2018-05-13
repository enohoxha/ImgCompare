package com.diff.images.Models;

public class CompareModel {
    private int id;
    private int imgOne;
    private int imgTwo;
    private double similarity;

    public CompareModel(int imgOne, int imgTwo) {
        this.imgOne = imgOne;
        this.imgTwo = imgTwo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgOne() {
        return imgOne;
    }

    public void setImgOne(int imgOne) {
        this.imgOne = imgOne;
    }

    public int getImgTwo() {
        return imgTwo;
    }

    public void setImgTwo(int imgTwo) {
        this.imgTwo = imgTwo;
    }

    public double getSimilarity() {
        return similarity;
    }

    public void setSimilarity(double similarity) {
        this.similarity = similarity;
    }
}

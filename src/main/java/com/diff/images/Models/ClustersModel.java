package com.diff.images.Models;

public class ClustersModel {

    private int id;
    private int pixelCount;
    private int red;
    private int green;
    private int blue;
    private int reds;
    private int greens;
    private int blues;

    public ClustersModel(int id, int rgb) {

        this.id = id;

        // shift colours to bit range than bitwise
        int r = (rgb>>16) & 0x000000FF;
        int g = (rgb>> 8) & 0x000000FF;
        int b = (rgb>> 0) & 0x000000FF;

        red = r;
        green = g;
        blue = b;
        addPixel(rgb);
    }

    public void clear() {
        // initialize to 0
        red = 0;
        green = 0;
        blue = 0;
        reds = 0;
        greens = 0;
        blues = 0;
        pixelCount = 0;
    }

    public int getId() {
        return id;
    }

    public int getRGB() {

        int r = reds / pixelCount;
        int g = greens / pixelCount;
        int b = blues / pixelCount;

        return 0xff000000|r<<16|g<<8|b;
    }

    public void addPixel(int color) {

        // shift colours to bit range than bitwise
        int r = (color>>16) & 0x000000FF;
        int g = (color>> 8) & 0x000000FF;
        int b = (color>> 0) & 0x000000FF;


        reds+=r;
        greens+=g;
        blues+=b;

        pixelCount++;

        red   = reds/pixelCount;
        green = greens/pixelCount;
        blue  = blues/pixelCount;
    }
    public void removePixel(int color) {

        // shift colours to bit range than bitwise
        int r = (color>>16) & 0x000000FF;
        int g = (color>> 8) & 0x000000FF;
        int b = (color>> 0) & 0x000000FF;

        reds-=r;
        greens-=g;
        blues-=b;
        pixelCount--;

        red   = reds/pixelCount;
        green = greens/pixelCount;
        blue  = blues/pixelCount;
    }

    public double distance(int color) {

        // shift colours to bit range than bitwise
        int r = (color>>16) & 0x000000FF;
        int g = (color>> 8) & 0x000000FF;
        int b = (color>> 0) & 0x000000FF;

        /*int rx = Math.abs(red-r);
        int gx = Math.abs(green-g);
        int bx = Math.abs(blue-b);*/

        double d = Math.sqrt( (red-r)*(red-r) + (green-g) * (green-g) +  (blue-b) * (blue-b) );
        return d;

    }

    public String toString(){

        return
                "Cluster id : "+ id +"\n" +
                "Pixels: "+ pixelCount + "\n" +
                "RGB: "+ getRGB();
    }

}

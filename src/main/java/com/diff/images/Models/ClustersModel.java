package com.diff.images.Models;

public class ClustersModel {

    int id;
    int pixelCount;
    int red;
    int green;
    int blue;
    int reds;
    int greens;
    int blues;

    public ClustersModel(int id, int rgb) {
        int r = rgb>>16&0x000000FF;
        int g = rgb>> 8&0x000000FF;
        int b = rgb>> 0&0x000000FF;
        red = r;
        green = g;
        blue = b;
        this.id = id;
        addPixel(rgb);
    }
    public void clear() {
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
        int r = color>>16&0x000000FF;
        int g = color>> 8&0x000000FF;
        int b = color>> 0&0x000000FF;
        reds+=r;
        greens+=g;
        blues+=b;
        pixelCount++;
        red   = reds/pixelCount;
        green = greens/pixelCount;
        blue  = blues/pixelCount;
    }
    public void removePixel(int color) {
        int r = color>>16&0x000000FF;
        int g = color>> 8&0x000000FF;
        int b = color>> 0&0x000000FF;

        reds-=r;
        greens-=g;
        blues-=b;
        pixelCount--;
        red   = reds/pixelCount;
        green = greens/pixelCount;
        blue  = blues/pixelCount;
    }

    public int distance(int color) {

        int r = color>>16&0x000000FF;
        int g = color>> 8&0x000000FF;
        int b = color>> 0&0x000000FF;

        int rx = Math.abs(red-r);
        int gx = Math.abs(green-g);
        int bx = Math.abs(blue-b);

        int d = (rx+gx+bx) / 3;
        return d;

    }

    public String toString(){

        return "Data: \n" +
                "Red: "+ red + "\n" +
                "Reds: "+ reds + "\n" +
                "Green: "+ green + "\n" +
                "Greens: "+ greens + "\n" +
                "Blue: "+ blues + "\n" +
                "blues: "+ blues + "\n" +
                "Pixels: "+ pixelCount + "\n" +
                "RGB: "+ getRGB();
    }
}

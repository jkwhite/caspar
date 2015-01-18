package org.excelsi.caspar.ca;


import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.PixelFormat;


public class WritableImagePlane implements Plane {
    private final WritableImage _i;
    private final PixelReader _r;
    private final PixelWriter _w;
    private final int _width;
    private final int _height;


    public WritableImagePlane(int w, int h) {
        _i = new WritableImage(w, h);
        _r = _i.getPixelReader();
        _w = _i.getPixelWriter();
        _width = w;
        _height = h;
    }

    public int getWidth() {
        return _width;
    }

    public int getHeight() {
        return _height;
    }

    public void init() {
    }

    public Image toImage() {
        return _i;
    }

    public void set(int x, int y, int v) {
        _w.setArgb(x, y, v);
    }

    public int get(int x, int y) {
        return _r.getArgb(x, y);
    }

    //public int[] getRow(int[] into, int y) {
        //return _i.getRGB(0, y, into.length, 1, into, 0, 0);
    //}

    public int[] getRow(int[] into, int y, int offset) {
        //return _i.getRGB(0, y, into.length-2*offset, 1, into, offset, 0);
        _r.getPixels(0, y, into.length-2*offset, 1,
            WritablePixelFormat.getIntArgbInstance(), into, offset, getWidth());
        return into;
    }

    //public int[] getBlock(int[] into, int x, int y, int d) {
        //return getBlock(into, x, y, d, 0);
    //}
//
    //public int[] getBlock(int[] into, int x, int y, int d, int offset) {
        //return _i.getRGB(x, y, d, d, into, offset, 0);
    //}
//
    //public int[] getBlock(int[] into, int x, int y, int dx, int dy, int offset) {
        //return _i.getRGB(x, y, dx, dy, into, offset, 0);
    //}

    public void setRow(int[] row, int y) {
        //_i.setRGB(0, y, row.length, 1, row, 0, 0);
        _w.setPixels(0, y, row.length, 1, PixelFormat.getIntArgbInstance(), row, 0, getWidth());
    }
}

package org.excelsi.caspar.ca;


import javafx.scene.image.Image;


public interface Plane {
    public int getWidth();
    public int getHeight();
    public void init();
    public void set(int x, int y, int v);
    public int get(int x, int y);
    //public int[] getRow(int[] into, int y);
    public int[] getRow(int[] into, int y, int offset);
    //public int[] getBlock(int[] into, int x, int y, int d);
    //public int[] getBlock(int[] into, int x, int y, int d, int offset);
    //public int[] getBlock(int[] into, int x, int y, int dx, int dy, int offset);
    public void setRow(int[] row, int y);
    public Image toImage();
}

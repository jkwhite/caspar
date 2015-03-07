package org.excelsi.caspar;


import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;


public class Screens {
    public static int[] size() {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        return new int[]{(int)screen.getWidth(), (int)screen.getHeight()};
    }
}

package org.excelsi.caspar;


import org.excelsi.caspar.ca.*;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;


public class CAView extends Parent {
    private CA _ca;
    private Projection _proj;
    private final int _i;
    private final int _j;
    private double _size;
    private Node _view;


    public CAView(CA ca, double size, Projection proj) {
        this(ca, -1, -1, size, proj);
    }

    public CAView(CA ca, int i, int j, double size, Projection proj) {
        _proj = proj;
        _ca = ca;
        _i = i;
        _j = j;
        _size = size;
        createCA();
    }

    //public CAView copy(double nsize) {
        //return new CAView(_ca, _i, _j, nsize);
    //}
//
    public void resizeCA(int width, int height) {
        _ca.resize(width, height);
        createCA();
    }

    private void createCA() {
        if(_view!=null) {
            getChildren().remove(_view);
        }
        _view = _proj.create(_ca, _size);
        /*
        Plane plane = _ca.createPlane();
        _view = new ImageView(plane.toImage());
        _view.setSmooth(true);
        _view.setPreserveRatio(true);
        _view.setCache(true);
        _view.setFitWidth(_size);
        */
        //final Node placeholder = new Rectangle(_size, _size, Color.rgb(255,0,0,0.5));
        //getChildren().add(placeholder);
        getChildren().add(_view);
    }

    public void setSmooth(boolean smooth) {
        //_view.setSmooth(smooth);
    }

    public CA getCA() {
        return _ca;
    }

    public int getI() {
        return _i;
    }

    public int getJ() {
        return _j;
    }

    public double getCASize() {
        return _size;
    }
}

package org.excelsi.caspar;


import java.util.Random;
import org.excelsi.caspar.ca.*;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.scene.paint.*;
import javafx.animation.*;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import javafx.geometry.*;


public enum Projection {
    rectangle,
    sphere;


    public Node create(CA ca, double size) {
        switch(this) {
            case rectangle:
                return createRectangle(ca, size);
            case sphere:
                return createSphere(ca, size);
        }
        return new Sphere(10);
    }

    public static Projection random(Random om) {
        switch(om.nextInt(2)) {
            case 0:
                return Projection.rectangle;
            default:
            case 1:
                return Projection.sphere;
        }
    }

    private Node createRectangle(CA ca, double size) {
        Plane plane = ca.createPlane();
        ImageView view = new ImageView(plane.toImage());
        view.setSmooth(true);
        view.setPreserveRatio(true);
        view.setCache(true);
        view.setFitWidth(size);
        return view;
    }

    private Node createSphere(CA ca, double size) {
        Plane plane = ca.createPlane();
        //ImageView view = new ImageView(plane.toImage());
        Sphere s = new Sphere(size/2); /* radius */
        PhongMaterial m = new PhongMaterial();
        m.setDiffuseMap(plane.toImage());
        s.setMaterial(m);
        RotateTransition rt = new RotateTransition(Duration.millis(10000), s);
        rt.setAxis(new Point3D(0,1,0));
        rt.setInterpolator(Interpolator.LINEAR);
        rt.setByAngle(360);
        rt.setCycleCount(42);
        //rt.setAutoReverse(true);
        rt.play();
        return s;
    }
}

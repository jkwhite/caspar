package org.excelsi.caspar;


import java.util.*;

import javafx.concurrent.*;
import javafx.scene.paint.Color;
import javafx.scene.*;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.transform.Rotate;
import javafx.stage.Screen;

import org.excelsi.caspar.ca.*;


public class Controls extends Parent {
    enum Mode { futures, scroll, sphere };
    //private final Futures _futures;
    private State _state;
    private Mode _mode;
    private Node _current;


    public Controls(State state) {
        _state = state;
        //_futures = futures;
        //_mode = Mode.futures;
        //setTranslateX(-150);
        //getChildren().add(futures);
        final Group ctrl = new Group();
        createControls(ctrl);
        toFutures(state);
        getChildren().add(ctrl);
    }

    private static Node createPlaceholder(double size) {
        return new Rectangle(size, size, Color.rgb(0,0,0,0));
    }

    private void createControls(Group g) {
        Node n = createButton();
        n.setTranslateY(30);
        n.setTranslateX(200+10+100);
        g.getChildren().add(n);
        n.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                if(_current instanceof Futures) {
                    ((Futures)_current).cycleCASize();
                }
            }
        });
        Node sc = createButton();
        sc.setTranslateX(30);
        sc.setTranslateY(200+10+100);
        g.getChildren().add(sc);
        sc.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                switch(_mode) {
                    case futures:
                        toSphere(timeline().getState());
                        break;
                    case scroll:
                        toFutures(timeline().getState());
                        break;
                    case sphere:
                        toScroll(timeline().getState());
                        break;
                }
            }
        });
    }

    private void toSphere(State state) {
        removeCurrent();
        addCurrent(new Spheres(state));
        _mode = Mode.sphere;
    }

    private void toFutures(State state) {
        removeCurrent();
        addCurrent(new Futures(state));
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        _current.setTranslateX(screen.getWidth()/2 - (3*200+2*10)/2);
        _current.setTranslateY(screen.getHeight()/2 - (3*200+2*10)/2-20);
        _mode = Mode.futures;
    }

    private void toScroll(State state) {
        removeCurrent();
        //CAView cav = _futures.getCenter().copy(1000);
        //cav.resizeCA(1000, 1000);
        //Scroll scroll = new Scroll(new Rand(), cav);
        //getChildren().remove(_futures);
        //add(scroll, 2, 0);
        //scroll.getTransforms().add(new Rotate(30, 50, 30));

        GroupTimeline g = new GroupTimeline();

        int rot = -90;
        Scroll scroll = new Scroll(state);
        scroll.getTransforms().add(new Rotate(rot, new Point3D(1,0,0)));
        scroll.getTransforms().add(new Rotate(180, new Point3D(0,0,1)));
        scroll.setTranslateY(500);
        scroll.setTranslateX(1280/2-state.size()/2);
        scroll.setTranslateZ(1000);
        g.getChildren().add(scroll);

        Scroll scroll2 = new Scroll(state);
        scroll2.getTransforms().add(new Rotate(rot, new Point3D(1,0,0)));
        scroll2.getTransforms().add(new Rotate(180, new Point3D(0,0,1)));
        scroll2.setTranslateY(100);
        scroll2.setTranslateX(1280/2-state.size()/2);
        scroll2.setTranslateZ(1000);
        g.getChildren().add(scroll2);
        //addCurrent(scroll);
        addCurrent(g);
        _mode = Mode.scroll;
    }

    private void removeCurrent() {
        if(_current!=null) {
            getChildren().remove(_current);
            timeline().dispose();
        }
    }

    private void addCurrent(Node current) {
        getChildren().add(current);
        _current = current;
    }

    private Timeline timeline() {
        return (Timeline) _current;
    }

    private static Circle createButton() {
        Circle ico = new Circle(10);
        ico.setFill(Color.rgb(10,10,10));
        ico.setStroke(Color.WHITE);
        ico.setStrokeWidth(3);
        return ico;
    }
}

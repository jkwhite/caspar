package org.excelsi.caspar;


import java.util.*;

import javafx.concurrent.*;
import javafx.scene.paint.*;
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
import javafx.collections.ObservableList;
import java.util.concurrent.atomic.AtomicReference;

import org.excelsi.caspar.ca.*;


public class Spheres extends Parent implements Timeline {
    private static final int FARPLANE = 8000;
    private static final int SLOPE = 2;
    private static final int MAX_SIZE = 300;
    private static final int SPEED = 16000;
    private static final int DIRECTED_SPEED = 2000;
    private State _state;
    private Random _om;
    private final MutatorFactory _mutators = MutatorFactory.instance();
    private Projection _proj = Projection.sphere;
    private final AtomicReference<Transition> _next = new AtomicReference<>();
    private final AtomicReference<Node> _target = new AtomicReference<>();
    private GParent _root;
    private int _xangle = 20;


    public Spheres(State state) {
        //setPickOnBounds(false);
        _state = state;
        _om = _state.random().create();
        _root = new GParent();
        getChildren().add(_root);
        createField(null, false);
        rotateRoot();
    }

    static class GParent extends Parent {
        public ObservableList<Node> getChildren() {
            return super.getChildren();
        }
    }

    private void rotateRoot() {
        RotateTransition rt = new RotateTransition(Duration.millis(10000), _root);
        //rt.setAxis(new Point3D(_om.nextDouble(),_om.nextDouble(),0));
        rt.setAxis(new Point3D(1,1,0));
        //rt.setInterpolator(Interpolator.LINEAR);
        rt.setFromAngle(_xangle);
        _xangle = -_xangle;
        rt.setToAngle(_xangle);
        //rt.setByAngle(_xangle);
        rt.setCycleCount(0);
        //rt.setAutoReverse(true);
        rt.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                rotateRoot();
            }
        });
        rt.play();
    }

    public void setState(State state) {
    }

    public State getState() {
        return _state;
    }

    public void dispose() {
    }

    private void translateOut(final Node old, boolean directed) {
        TranslateTransition tt = new TranslateTransition(Duration.millis(directed?DIRECTED_SPEED:SPEED), old);
        tt.setInterpolator(Interpolator.LINEAR);
        tt.setByZ(-FARPLANE/2);
        int[] dims = Screens.size();
        //if(directed) {
            //tt.setByX(-SLOPE * (seed.getTranslateX()-dims[0])/2);
            //tt.setByY(-SLOPE * (seed.getTranslateY()-dims[1])/2);
        //}
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                _root.getChildren().remove(old);
            }
        });
        //createField(s);
        tt.play();
    }

    private void createField(Node seed, boolean directed) {
        /*
        if(seed!=null) {
            final Node old = seed.getParent();
            TranslateTransition tt = new TranslateTransition(Duration.millis(directed?DIRECTED_SPEED:SPEED), old);
            tt.setInterpolator(Interpolator.LINEAR);
            tt.setByZ(-FARPLANE/2);
            int[] dims = Screens.size();
            //if(directed) {
                //tt.setByX(-SLOPE * (seed.getTranslateX()-dims[0])/2);
                //tt.setByY(-SLOPE * (seed.getTranslateY()-dims[1])/2);
            //}
            tt.setOnFinished(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e) {
                    getChildren().remove(old);
                }
            });
            //createField(s);
            tt.play();
        }
        */
        final Group g = new Group();
        g.setPickOnBounds(false);
        for(int i=0;i<30;i++) {
            addObject(g);
        }
        //FadeTransition ft = new FadeTransition(Duration.millis(3000), g);
        //ft.setFromValue(0.0);
        //ft.setToValue(1.0);
        g.setTranslateZ(FARPLANE);
        int[] dims = Screens.size();
        if(seed!=null&&directed) {
            g.setTranslateX(SLOPE * (seed.getTranslateX()-dims[0]));
            g.setTranslateY(SLOPE * (seed.getTranslateY()-dims[1]));
        }
        final TranslateTransition tt = new TranslateTransition(Duration.millis(directed?DIRECTED_SPEED:SPEED), g);
        tt.setInterpolator(Interpolator.LINEAR);
        tt.setByZ(-FARPLANE);
        if(seed!=null&&directed) {
            tt.setByX(-SLOPE * (seed.getTranslateX()-dims[0]));
            tt.setByY(-SLOPE * (seed.getTranslateY()-dims[1]));
        }
        _root.getChildren().add(g);
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Node sel = _target.get();
                boolean d = false;
                if(sel!=null) {
                    _target.set(null);
                    d = true;
                }
                else {
                    sel = g.getChildren().get(0);
                }
                //createField(sel, d);
                translateOut(g, false);
                createField(sel, false);
            /*
                if(_next.get()==tt) {
                    createField(g.getChildren().get(0), false);
                }
                else {
                    translateOut(g, false);
                }
                */
                
            }
        });
        _next.set(tt);
        tt.play();
        //ft.play();
    }

    private void addObject(final Group g) {
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        Rule rule = _mutators.createRandomMutator(_om).mutate(_state.ca().getRule());
        final CA ca = new CA(rule, new RandomInitializer(), _state.random().create(), 0, _state.size(), _state.size());
        _state.ca(ca);
        final Node s = _proj.create(ca, _om.nextInt(MAX_SIZE)+30);
        //s.setPickOnBounds(false);
        g.getChildren().add(s);
        do {
            int sw = (int) screen.getWidth(), sh = (int) screen.getHeight();
            s.setTranslateX(_om.nextInt(2*sw)-sw/2);
            s.setTranslateY(_om.nextInt(2*sh)-sh/2);
            s.setTranslateZ(_om.nextInt(4000));
        } while(collides(g, s));
        s.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                Transition t = _next.get();
                if(t!=null) {
                    //t.stop();
                    //t.setRate(4.0);
                }
                _target.set(s);
                _state.ca(ca);
                //createField(s, true);
                /*
                Node old = g;
                FadeTransition ft = new FadeTransition(Duration.millis(SPEED), old);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                */
                Node old = g;
                TranslateTransition tt = new TranslateTransition(Duration.millis(SPEED), s);
                //tt.setByZ(-FARPLANE/2);
                int[] dims = Screens.size();
                tt.setByX(-SLOPE * (s.getTranslateX()-dims[0])/2);
                tt.setByY(-SLOPE * (s.getTranslateY()-dims[1])/2);
                /*
                tt.setOnFinished(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        getChildren().remove(g);
                    }
                });
                _state.ca(ca);
                createField(s);
                */
                //tt.play();
                RotateTransition rt = new RotateTransition(Duration.millis(3000), s);
                rt.setAxis(new Point3D(0,1,0));
                rt.setInterpolator(Interpolator.LINEAR);
                rt.setByAngle(360);
                rt.setCycleCount(420);
                //rt.setAutoReverse(true);
                rt.play();
            }
        });
    }

    private boolean collides(Group g, Node n) {
        Bounds nb = n.getBoundsInParent();
        for(int i=0;i<g.getChildren().size();i++) {
            Node c = g.getChildren().get(i);
            if(c!=n && c.getBoundsInParent().contains(nb)) {
                System.err.println("COLI: "+nb+", "+c.getBoundsInParent());
                return true;
            }
        }
        return false;
    }
}

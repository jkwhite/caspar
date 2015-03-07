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
import javafx.geometry.Pos;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

import org.excelsi.caspar.ca.*;


public class Futures extends GridPane implements Timeline {
    enum Mode { open, single };
    private static final Image CA_PLACEHOLDER = new CA(new Ruleset1D(new int[]{Colors.pack(0,0,0,0), Colors.pack(0,0,1,0)}).iterator().next(), new RandomInitializer(), new Random(), 0, 100, 100).createPlane().toImage();
    private static final int CA_SIZE = 400;
    private static final int VIEW_SIZE = 200;
    private static final int FADE_IN = 400;
    private static final int GAP = 10;
    private final Random _om;
    private final MutatorFactory _mutator = MutatorFactory.instance();
    private long _seed = 0;
    private final List<FutureListener> _listeners = new ArrayList<>();
    private int _caSize;
    private final List<Animation> _squareAnimations = new Vector<>();
    private Mode _mode = Mode.open;
    private Projection _proj = Projection.rectangle;
    private State _state;


    public Futures(State state) {
        setPickOnBounds(false);
        _state = state;
        _caSize = _state.size();
        //this(state.rules(), state.random());
        setAlignment(Pos.CENTER);
        setHgap(GAP);
        setVgap(GAP);
        _om = createRandom();
        if(state.ca()!=null) {
            choose(state.ca());
        }
        else {
            int i=0, j=0;
            Iterator<Rule> it = _state.rules().random(createRandom());
            int total = 0;
            {
                Rule rule = it.next();
                CA ca = new CA(rule, new RandomInitializer(), createRandom(), _seed, _caSize, _caSize);
                createPossibility(ca, 1, 1, false, true);
            }
            while(total<9) {
                if(!(i==1&&j==1)) {
                    Rule rule = it.next();
                    CA ca = new CA(rule, new RandomInitializer(), createRandom(), _seed, _caSize, _caSize);
                    createPossibility(ca, i, j, true, true);
                }
                if(++i==3) {
                    i = 0;
                    j++;
                }
                total++;
            }
        }
    }

    /*
    public Futures(Ruleset ruleset, RandomFactory rand) {
        setAlignment(Pos.CENTER);
        setHgap(GAP);
        setVgap(GAP);
        _ruleset = ruleset;
        _rand = rand;
        _om = createRandom();
        int i=0, j=0;
        Iterator<Rule> it = _ruleset.random(createRandom());
        int total = 0;
        {
            Rule rule = it.next();
            CA ca = new CA(rule, new RandomInitializer(), createRandom(), _seed, _caSize, _caSize);
            createPossibility(ca, 1, 1, false, true);
        }
        while(total<9) {
            if(!(i==1&&j==1)) {
                Rule rule = it.next();
                CA ca = new CA(rule, new RandomInitializer(), createRandom(), _seed, _caSize, _caSize);
                createPossibility(ca, i, j, true, true);
            }
            if(++i==3) {
                i = 0;
                j++;
            }
            total++;
        }
    }
    */

    public State getState() {
        return _state;
        /*
        CAView cav = getCenter();
        return new State()
            .ca(cav.getCA())
            .rules(cav.getCA().getRule().origin())
            .random(_rand)
            .size(cav.getCASize());
            */
    }

    public void setState(State state) {
        choose(state.ca());
    }

    public void dispose() {
    }

    public CAView getCenter() {
        return (CAView) getChildren().get(4);
    }

    public void addFutureListener(FutureListener listener) {
        _listeners.add(listener);
    }

    public int getPossibilityWidth() {
        return 3 * VIEW_SIZE + 2*GAP;
    }

    public int getPossiblityHeight() {
        return 3 * VIEW_SIZE + 2*GAP;
    }

    public void cycleCASize() {
        if(_mode==Mode.single) {
            System.err.println("size: "+_caSize);
            if(_caSize<5000) {
                _caSize *= 2;
            }
            else {
                _caSize = 200;
            }
            CAView v = getCA(1, 1);
            v.resizeCA(_caSize, _caSize);
            _state.size(_caSize);
        }
    }

    private CAView getCA(int i, int j) {
        for(Node n:getChildren()) {
            CAView v = (CAView) n;
            if(v.getI()==i && v.getJ()==j) {
                return (CAView) v;
            }
        }
        return null;
    }

    private void choose(final CA ca) {
        _state.ca(ca);
        final Rule rule = ca.getRule();
        notifyListeners();
        // TODO: JavaFX bug causes removeAll to not remove any children...
        //getChildren().removeAll();
        while(!getChildren().isEmpty()) {
            getChildren().remove(0);
        }
        if(_om.nextInt(10)==8) {
            _proj = Projection.sphere;
        }
        else {
            _proj = Projection.rectangle;
        }
        int i=0, j=0;
        int total = 0;
        while(total<9) {
            Rule cur = null;
            if(total==4) {
                cur = rule;
            }
            else {
                int tries = 0;
                while(cur==null && ++tries<10) {
                    try {
                        cur = _mutator.createRandomMutator(_om).mutate((Multirule)rule);
                    }
                    catch(MutationFailedException e) {
                    }
                }
                if(cur==null) {
                    cur = rule;
                }
            }
            CA nca = new CA(cur, new RandomInitializer(), createRandom(), _seed, _caSize, _caSize);
            createPossibility(nca, i, j, total!=4, total!=4);
            if(++i==3) {
                i = 0;
                j++;
            }
            total++;
        }
    }

    private void createPossibility(final CA ca, final int i, final int j, final boolean async, final boolean fade) {
        if(async) {
            //final Node placeholder = new ImageView(CA_PLACEHOLDER);
            //placeholder.setPreserveRatio(true);
            //placeholder.setFitWidth(VIEW_SIZE);
            final Node placeholder = new Rectangle(VIEW_SIZE, VIEW_SIZE, Color.rgb(0,0,0,0));
            add(placeholder, i, j);
            Service<Void> svc = new Service<Void>() {
                private Node _view;

                protected Task createTask() {
                    return new Task<Void>() {
                        protected Void call() throws Exception {
                            _view = createView(ca, i , j);
                            return null;
                        }
                    };
                }

                protected void succeeded() {
                    Futures.this.getChildren().remove(placeholder);
                    Futures.this.add(_view, i, j);
                    if(fade) {
                        FadeTransition ft = new FadeTransition(Duration.millis(FADE_IN), _view);
                        ft.setFromValue(0.0);
                        ft.setToValue(1.0);
                        ft.play();
                    }
                }
            };
            svc.start();
        }
        else {
            Node view = createView(ca, i, j);
            add(view, i, j);
            if(fade) {
                FadeTransition ft = new FadeTransition(Duration.millis(FADE_IN), view);
                ft.setFromValue(0.0);
                ft.setToValue(1.0);
                ft.play();
            }
        }
        /*
        Plane plane = ca.createPlane();
        final ImageView imview = new ImageView(plane.toImage());
        imview.setSmooth(true);
        imview.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                select(imview, ca, i, j);
            }
        });
        return imview;
        */
    }

    private Node createView(final CA ca, final int i, final int j) {
        CAView v = new CAView(ca, i, j, VIEW_SIZE, _proj);
        /*
        Plane plane = ca.createPlane();
        final ImageView imview = new ImageView(plane.toImage());
        imview.setSmooth(true);
        imview.setPreserveRatio(true);
        imview.setCache(false);
        imview.setFitWidth(VIEW_SIZE);
        createHandler(imview, ca, i, j);
        */
        TranslateTransition tt = new TranslateTransition(Duration.millis(5000), v);
        tt.setByY(20);
        tt.setDelay(Duration.seconds(30));
        tt.setAutoReverse(true);
        tt.setCycleCount(100);
        tt.play();
        _squareAnimations.add(tt);
        v.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                System.err.println("HERE: "+e);
                if(i==1&&j==1) {
                    expand(v, ca, i, j, 1, 3);
                }
                else {
                    select(v, ca, i, j);
                }
            }
        });
        return v;
    }

    private void createHandler(final CAView node) {
        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                if(node.getI()==1&&node.getJ()==1) {
                    expand(node, node.getCA(), node.getI(), node.getJ(), 1, 3);
                }
                else {
                    select(node, node.getCA(), node.getI(), node.getJ());
                }
            }
        });
    }

    private void contract(final CAView node, final CA ca, final int i, final int j, final double from, final double to) {
        List<Transition> fts = new ArrayList<>();
        if(to==1) {
            _mode = Mode.open;
            for(Node n:getChildren()) {
                if(n!=node) {
                    createHandler((CAView)n);
                    FadeTransition ft = new FadeTransition(Duration.millis(FADE_IN), n);
                    ft.setFromValue(0.0);
                    ft.setToValue(1.0);
                    fts.add(ft);
                }
            }
        }
        ScaleTransition scale = new ScaleTransition(Duration.millis(FADE_IN), node);
        scale.setFromX(from);
        scale.setFromY(from);
        //scale.setFromZ(from);
        scale.setToX(to);
        scale.setToY(to);
        //scale.setToZ(to);
        fts.add(scale);
        ParallelTransition pt = new ParallelTransition(fts.toArray(new Transition[0]));
        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                if(to==1) {
                    expand(node, ca, i, j, 1, 3);
                }
                else {
                    contract(node, ca, i, j, to, to/3);
                }
            }
        });
        pt.play();
    }

    private void expand(final CAView node, final CA ca, final int i, final int j, final double from, final double to) {
        List<Transition> fts = new ArrayList<>();
        if(from==1) {
            _mode = Mode.single;
            for(Node n:getChildren()) {
                if(n!=node) {
                    n.setOnMouseClicked(null);
                    FadeTransition ft = new FadeTransition(Duration.millis(FADE_IN), n);
                    ft.setFromValue(1.0);
                    ft.setToValue(0.0);
                    fts.add(ft);
                }
            }
        }
        ScaleTransition scale = new ScaleTransition(Duration.millis(FADE_IN), node);
        scale.setFromX(from);
        scale.setFromY(from);
        //scale.setFromZ(from);
        scale.setToX(to);
        scale.setToY(to);
        //scale.setToZ(to);
        //Transition img = new FitWidthTransition(node, VIEW_SIZE, 3*VIEW_SIZE, Duration.millis(FADE_IN));
        //TranslateTransition mv = new TranslateTransition(Duration.millis(FADE_IN), node);
        //mv.setByX(-VIEW_SIZE-GAP);
        //mv.setByY(-VIEW_SIZE-GAP);
        fts.add(scale);
        //fts.add(mv);
        //fts.add(img);
        ParallelTransition pt = new ParallelTransition(fts.toArray(new Transition[0]));
        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                if(from<10) {
                    node.setSmooth(false);
                    expand(node, ca, i, j, to, 3*to);
                }
                else {
                    node.setSmooth(true);
                    contract(node, ca, i, j, to, 1);
                }
            }
        });
        pt.play();
    }

    private void select(final CAView node, final CA ca, final int i, final int j) {
        List<Transition> fts = new ArrayList<>();
        for(Node n:getChildren()) {
            if(n!=node) {
                FadeTransition ft = new FadeTransition(Duration.millis(FADE_IN), n);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                fts.add(ft);
            }
        }
        while(!_squareAnimations.isEmpty()) {
            _squareAnimations.remove(0).stop();
        }
        ParallelTransition pt = new ParallelTransition(fts.toArray(new Transition[0]));

        TranslateTransition tt = new TranslateTransition(Duration.millis(600), node);
        tt.setByX(-(VIEW_SIZE+GAP)*(i-1));
        tt.setByY(-(VIEW_SIZE+GAP)*(j-1));
        //tt.setToX(VIEW_SIZE+GAP);
        //tt.setToY(VIEW_SIZE+GAP);
        SequentialTransition st = new SequentialTransition(pt, tt);
        st.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                choose(ca);
                getChildren().remove(node);
            }
        });
        st.play();
    }

    private Random createRandom() {
        return _state.random().create();
    }

    private void notifyListeners() {
        for(FutureListener listener:_listeners) {
            listener.chosen();
        }
    }
}

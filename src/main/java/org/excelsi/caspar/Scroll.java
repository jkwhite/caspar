package org.excelsi.caspar;


import java.util.List;
import java.util.ArrayList;
import org.excelsi.caspar.ca.*;
import java.util.Random;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.*;


public class Scroll extends Parent implements Timeline {
    private final MutatorFactory _mutator = MutatorFactory.instance();
    private final Random _om;
    private final CAView _init;
    private int _ahead = 12;
    private boolean _stop = false;
    private Projection _proj;
    private State _state;


    public Scroll(State state) {
        _state = state;
        _proj = Projection.random(state.random().create());
        _init = new CAView(state.ca(), state.size(), _proj);
        _om = state.random().create();
        lookAhead(_init);
        //this(state.random(), new CAView(state.recent(), state.size()));
        //this(state.random(), new CAView(state.recent(), state.recent().getWidth()));
    }

    public State getState() {
        return _state;
        /*
        CAView cav = lastParent();
        return new State()
            .recent(cav.getCA())
            .rules(cav.getCA().getRule().origin())
            .random(_rand)
            .size(cav.getCASize());
            */
    }

    public void setState(State state) {
    }

    public void dispose() {
        _stop = true;
    }

    private synchronized void lookAhead(CAView init) {
        if(!_stop) {
            int initIdx = getChildren().size();
            while(getChildren().size()<_ahead) {
                //CAView parent = getChildren().isEmpty() ? init : (CAView)((Group)getChildren().get(getChildren().size()-1)).getChildren().get(0);
                CAView parent = init;
                final CAView c = createChild(parent);
                addNewChild(c);
                init = c;
            }
            if(initIdx<getChildren().size()) {
                List<Transition> ts = new ArrayList<Transition>();
                for(int i=initIdx;i<getChildren().size();i++) {
                    final Group g = (Group) getChildren().get(i);
                    final CAView c = (CAView) g.getChildren().get(0);
                    final int t = i+1;
                    TranslateTransition tt = new TranslateTransition(Duration.millis((int)(t*10000*0.7)), c);
                    tt.setFromY((int)(c.getCASize()*t));
                    tt.setToY(-10*c.getCASize());
                    tt.setInterpolator(Interpolator.LINEAR);
                    tt.setOnFinished(new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e) {
                            Scroll.this.getChildren().remove(g);
                            lookAhead(c);
                        }
                    });
                    ts.add(tt);
                }
                for(Transition tt:ts) {
                    tt.play();
                }
            }
        }
    }

    private CAView lastParent() {
        CAView parent = getChildren().isEmpty() ? _init : (CAView)((Group)getChildren().get(getChildren().size()-1)).getChildren().get(0);
        return parent;
    }

    private void addNewChild(final CAView c) {
        final Group g = new Group();
        getChildren().add(g);
        g.getChildren().add(c);
        _state.ca(c.getCA());
        /*
        final int t = getChildren().size();
        TranslateTransition tt = new TranslateTransition(Duration.millis(t*10000), c);
        tt.setFromY((int)(c.getCASize()*t*0.77));
        tt.setToY(-10*c.getCASize());
        tt.setInterpolator(Interpolator.LINEAR);
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Scroll.this.getChildren().remove(g);
                lookAhead(c);
            }
        });
        tt.play();
        */
    }

    private CAView createChild(CAView parent) {
        Rule cur = null;
        int tries = 0;
        while(cur==null && ++tries<10) {
            try {
                cur = _mutator.createRandomMutator(_om).mutate((Multirule)parent.getCA().getRule());
            }
            catch(MutationFailedException e) {
            }
        }
        if(cur==null) {
            cur = parent.getCA().getRule();
        }
        CA ca = new CA(cur, new RandomInitializer(), _state.random().create(), parent.getCA().getSeed(), parent.getCA().getWidth(), parent.getCA().getHeight());
        CAView v = new CAView(ca, 0, 0, parent.getCASize(), _proj);
        return v;
    }
}

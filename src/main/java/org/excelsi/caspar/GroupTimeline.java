package org.excelsi.caspar;


import javafx.scene.*;


public class GroupTimeline extends Group implements Timeline {
    private State _state;


    //public GroupTimeline(State state) {
        //_state = state;
    //}

    public void setState(State s) {
        for(Node n:getChildren()) {
            ((Timeline)n).setState(s);
        }
    }

    public State getState() {
        return ((Timeline)getChildren().get(0)).getState();
    }

    public void dispose() {
        for(Node n:getChildren()) {
            ((Timeline)n).dispose();
        }
    }
}

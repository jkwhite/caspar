package org.excelsi.caspar;


import java.util.*;
import org.excelsi.caspar.ca.*;


public final class State {
    public enum Field { ca, size, rules, rand };

    private CA _ca;
    private int _size = 200;
    private Ruleset _rules;
    private RandomFactory _rand;
    private final List<StateListener> _listeners = new ArrayList<>();
    private static final State _inst = new State();


    private State() {}

    public static State state() {
        return _inst;
    }

    public CA ca() {
        return _ca;
    }

    public State ca(CA ca) {
        _ca = ca;
        notifyListeners(Field.ca);
        return this;
    }

    public State size(int size) {
        _size = size;
        return this;
    }

    public int size() {
        return _size;
    }

    public State rules(Ruleset rules) {
        _rules = rules;
        return this;
    }

    public Ruleset rules() {
        return _rules;
    }

    public State random(RandomFactory rand) {
        _rand = rand;
        return this;
    }

    public RandomFactory random() {
        return _rand;
    }

    public void addListener(StateListener listener) {
        _listeners.add(listener);
    }

    private void notifyListeners(Field field) {
        for(int i=0;i<_listeners.size();i++) {
            _listeners.get(i).stateChanged(field);
        }
    }
}

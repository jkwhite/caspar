package org.excelsi.caspar.ca;


public enum Mutators {
    collapse,
    order,
    cull,
    fork,
    hue,
    color,
    expand,
    noise,
    tangle,
    diverge,
    thin,
    thicken,
    symmetry,
    transpose,
    stabilize,
    destabilize,
    random,
    life;


    public Mutator create() {
        switch(this) {
            case random:
                return new RandomMutator();
            case tangle:
                return new Tangle();
            case cull:
                return new Cull();
            case fork:
                return new Fork();
            case symmetry:
                return new Symmetry();
            case thicken:
                return new Thicken();
            case thin:
                return new Thin();
            case hue:
                return new Hue();
            case noise:
                return new Noise();
            case color:
                return new Color();
        }
        return null;
        /*
        switch(this) {
            case collapse:
                return new Collapse();
            case order:
                return new Order();
            case cull:
                return new Cull();
            case clone:
                return new Clone();
            case hue:
                return new Hue();
            case color:
                return new Color();
            case expand:
                return new Expand();
            case noise:
                return new Noise();
            case tangle:
                return new Tangle();
            case diverge:
                return new Diverge();
            case thin:
                return new Thin();
            case thicken:
                return new Thicken();
            case symmetry:
                return new Symmetry();
            case transpose:
                return new Transpose();
            case stabilize:
                return new Stability(1);
            case destabilize:
                return new Stability(-1);
            case life:
                return new Life();
        }
        */
    }
}

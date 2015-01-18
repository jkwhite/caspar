package org.excelsi.caspar.ca;


import java.util.Random;


public final class CA {
    private Rule _r;
    private Initializer _i;
    private int _w;
    private int _h;
    private Random _rand;


    public CA(Rule r, Initializer i, Random rand, int w, int h) {
        _r = r;
        _i = i;
        _rand = rand;
        _w = w;
        _h = h;
    }

    public Plane createPlane() {
        WritableImagePlane p = new WritableImagePlane(_w, _h);
        _i.init(p, _r, _rand);
        _r.generate(p, 1, _h, false, true, null);
        return p;
    }
}

package org.excelsi.caspar.ca;


import java.util.Random;


public final class CA {
    private Rule _r;
    private Initializer _i;
    private int _w;
    private int _h;
    private Random _rand;
    private long _seed;


    public CA(Rule r, Initializer i, Random rand, long seed, int w, int h) {
        _r = r;
        _i = i;
        _rand = rand;
        _seed = seed;
        _w = w;
        _h = h;
    }

    public Plane createPlane() {
        WritableImagePlane p = new WritableImagePlane(_w, _h);
        _rand.setSeed(_seed);
        _i.init(p, _r, _rand);
        if(_r instanceof Multirule1D) {
            ((Multirule1D)_r).generate2(p, 1, _h, false, true, null);
        }
        else {
            _r.generate(p, 1, _h, false, true, null);
        }
        return p;
    }

    public Rule getRule() {
        return _r;
    }

    public long getSeed() {
        return _seed;
    }

    public int getWidth() {
        return _w;
    }

    public int getHeight() {
        return _h;
    }

    public void resize(int w, int h) {
        _w = w;
        _h = h;
    }
}

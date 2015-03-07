package org.excelsi.caspar;


import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.image.ImageView;


public class FitWidthTransition extends Transition {
    private final ImageView _view;
    private final double _initSize;
    private final double _finalSize;


    public FitWidthTransition(ImageView view, double initSize, double finalSize, Duration d) {
        setCycleDuration(d);
        _view = view;
        _initSize = initSize;
        _finalSize = finalSize;
    }

    protected void interpolate(double frac) {
        _view.setFitWidth(_initSize*(1-frac)+_finalSize*frac);
    }
}

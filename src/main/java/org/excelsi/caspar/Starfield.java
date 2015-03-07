package org.excelsi.caspar;


import java.util.Random;
import javafx.util.Duration;
import javafx.animation.*;
import javafx.scene.Group;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class Starfield extends Group implements StateListener {
    private final int _x1;
    private final int _x2;
    private final int _y1;
    private final int _y2;
    private final Random _rand;
    private final int _count;


    public Starfield(Random rand, int x1, int y1, int x2, int y2, int count) {
        _x1 = x1;
        _y1 = y1;
        _x2 = x2;
        _y2 = y2;
        _count = count;
        _rand = rand;
        for(int i=0;i<count;i++) {
            int size = rand.nextInt(3)+1;
            if(size==3) {
                size = rand.nextInt(3)+1;
            }
            Circle circ = new Circle(rand.nextInt(-x1+x2)+x1, rand.nextInt(-y1+y2)+y1, size);
            circ.setFill(Color.rgb(200,200,200+rand.nextInt(56)));
            circ.setTranslateZ(1+rand.nextInt(40));
            getChildren().add(circ);
        }
    }

    public void stateChanged(State.Field field) {
        cycle();
    }

    public void cycle() {
        TranslateTransition tt = new TranslateTransition(Duration.millis(6000), this);
        tt.setByZ(-50);
        tt.setOnFinished(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                if(Starfield.this.getTranslateZ()<=500) {
                    Starfield.this.setTranslateZ(2000);
                }
            }
        });
        tt.play();
    }
}

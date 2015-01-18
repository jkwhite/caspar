package org.excelsi.caspar;


import java.util.Random;
import java.util.Iterator;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import org.excelsi.caspar.ca.*;


public class Main extends Application {
    public void start(Stage stage) {
        //Circle circ = new Circle(40, 40, 30);
        //Group root = new Group(circ);
        Random rand = new Random();

        Ruleset rules = new Ruleset1D(new int[]{Colors.randomColor(rand), Colors.randomColor(rand)});
        //Rule rule = rules.random(rand).next();
        Iterator<Rule> it = rules.iterator();
        //FlowPane flow = new FlowPane();
        //flow.setPrefWrapLength(1200);
        GridPane grid = new GridPane();
        int i=0, j=0;
        while(it.hasNext()) {
            Rule rule = it.next();
            CA ca = new CA(rule, new RandomInitializer(), rand, 100, 100);
            Plane plane = ca.createPlane();
            ImageView imview = new ImageView(plane.toImage());
            imview.setSmooth(true);
            //flow.getChildren().add(imview);
            grid.add(imview, i, j);
            if(++i==16) {
                i = 0;
                j++;
            }
        }
        //flow.setScaleX(0.5);
        //flow.setScaleY(0.5);
        grid.setScaleX(0.3);
        grid.setScaleY(0.3);
        //grid.setLayoutX(-400);
        //grid.setLayoutY(-400);
        BorderPane border = new BorderPane();
        //border.setTop(flow);
        border.setCenter(grid);
        Group root = new Group(border);
        root.setLayoutX(-400);
        root.setLayoutY(-400);
        Scene scene = new Scene(root, 1280, 1024);
        scene.setFill(Color.BLACK);

        stage.setTitle("CASPAR");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}

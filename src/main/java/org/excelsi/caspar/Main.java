package org.excelsi.caspar;


import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.EnumSet;

import javafx.application.*;
import javafx.geometry.Rectangle2D;
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
import javafx.event.Event;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;

import org.excelsi.caspar.ca.*;


public class Main extends Application {
    public void start(final Stage stage) {
        for(ConditionalFeature f:EnumSet.allOf(ConditionalFeature.class)) {
            System.err.println(f+": "+Platform.isSupported(f));
        }
        Rectangle2D screen = Screen.getPrimary().getVisualBounds();
        final Random rand = new Random();

        /*
        final Group starfield = new Group();
        for(int i=0;i<66;i++) {
            int size = rand.nextInt(3)+1;
            if(size==3) {
                size = rand.nextInt(3)+1;
            }
            Circle circ = new Circle(rand.nextInt((int)screen.getWidth()), rand.nextInt(200+(int)screen.getHeight())-200,
                size);
            circ.setFill(Color.rgb(200,200,200+rand.nextInt(56)));
            circ.setTranslateZ(1+rand.nextInt(40));
            starfield.getChildren().add(circ);
        }
        */
        final List<Starfield> stars = new ArrayList<>();
        for(int i=0;i<10;i++) {
            int sw = (int)screen.getWidth(), sh = (int)screen.getHeight();
            Starfield sf = new Starfield(rand, -sw, -sh, 2*sw, 2*sh, rand.nextInt(30)+10);
            sf.setTranslateZ(rand.nextInt(2000)+50);
            stars.add(sf);
        }
        //final Starfield starfield2 = new Starfield(rand, -200, -200, (int)screen.getWidth(), (int)screen.getHeight()+200, 40);

        final Ruleset1D rules = new Ruleset1D(new int[]{Colors.randomColor(rand), Colors.randomColor(rand)});
        final Ruleset rules2 = new Rulespace1D(rules);
        //Rule rule = rules.random(rand).next();
        Iterator<Rule> it = rules.iterator();
        GridPane gridp = new GridPane();
        int i=0, j=0;
        while(it.hasNext()) {
            Rule rule = it.next();
            CA ca = new CA(rule, new RandomInitializer(), rand, 42, 100, 100);
            Plane plane = ca.createPlane();
            ImageView imview = new ImageView(plane.toImage());
            imview.setSmooth(true);
            imview.setFitWidth(30);
            imview.setPreserveRatio(true);
            gridp.add(imview, i, j);
            if(++i==16) {
                i = 0;
                j++;
            }
        }
        //gridp.setScaleX(0.3);
        //gridp.setScaleY(0.3);
        //gridp.setPrefSize(100*3/3, 100*3/3);
        //gridp.setMaxSize(100*3/3, 100*3/3);

        final double XTRANS = screen.getWidth()/2-30*16/2;
        final double YTRANS = screen.getHeight()/2-30*16/2;
        //gridp.setTranslateX((screen.getWidth()/2+100*16/2)*0.3);
        //gridp.setTranslateX(0);
        gridp.setTranslateX(XTRANS);
        gridp.setTranslateY(YTRANS);
        //gridp.setAlignment(Pos.CENTER);
        Group grid = new Group(gridp);
        //grid.setTranslateX(0);
        //grid.setTranslateY(0);

        //gridp.relocate(-400, -400);
        //gridp.setTranslateX(-300);
        //gridp.setTranslateY(-150);

        /*
        final RotateTransition rt = new RotateTransition(Duration.millis(3000), gridp);
        rt.setByAngle(180);
        rt.setCycleCount(4);
        rt.setAutoReverse(true);
        */
        //rt.setAutoReverse(false);

        /*`
        final BorderPane border = new BorderPane();
        */
        //Label title = new Label("EXPLORATIONS IN CELLULAR SPACES");
        Label title = new Label("E  X  P  L  O  R  A  T  I  O  N  S");
        title.setFont(new Font("Helvetica Neue Condensed Bold", 36));
        title.setTextFill(Color.WHITE);
        //Label title2 = new Label("IN CELLULAR SPACES");
        Label title2 = new Label("EXPLORATIONS IN CELLULAR SPACES");
        title2.setFont(new Font("Helvetica Neue Condensed Bold", 28));
        title2.setTextFill(Color.WHITE);
        /*`
        title.setAlignment(Pos.CENTER);
        title.setContentDisplay(ContentDisplay.CENTER);
        title.setTextAlignment(TextAlignment.CENTER);
        */
        final HBox toptitle = new HBox();
        toptitle.setAlignment(Pos.CENTER);
        toptitle.getChildren().add(title);
        toptitle.setTranslateX(XTRANS);
        toptitle.setTranslateY(YTRANS-36);

        final HBox btitle = new HBox();
        btitle.setAlignment(Pos.CENTER);
        title2.setAlignment(Pos.CENTER);
        btitle.getChildren().add(title2);
        btitle.setTranslateX(XTRANS);
        //btitle.setTranslateX(screen.getWidth()/2-title2.getPrefWidth()/2);
        btitle.setTranslateY(YTRANS+30*16);

        Group border = new Group();
        //border.getChildren().add(toptitle);
        for(Starfield st:stars) {
            border.getChildren().add(st);
        }
        //border.getChildren().add(starfield2);
        border.getChildren().add(btitle);
        border.getChildren().add(grid);

        final List<TranslateTransition> tts = new ArrayList<>();
        final TranslateTransition tt = new TranslateTransition(Duration.millis(6000), grid);
        tt.setByY(2000);
        tts.add(tt);
        for(Starfield sf:stars) {
            TranslateTransition st = new TranslateTransition(Duration.millis(6000), sf);
            st.setByY(200);
            st.setByZ(100+rand.nextInt(100));
            tts.add(st);
        }
        /*
        final TranslateTransition tt2 = new TranslateTransition(Duration.millis(6000), starfield1);
        tt2.setByY(200);
        tt2.setByZ(200);
        final TranslateTransition tt3 = new TranslateTransition(Duration.millis(6000), starfield2);
        tt3.setByY(300);
        tt3.setByZ(200);
        */
        //final ParallelTransition infinite = new ParallelTransition(tt, tt2, tt3);
        final ParallelTransition infinite = new ParallelTransition(tts.toArray(new TranslateTransition[0]));

        final BorderPane ctrl = new BorderPane();
        //ctrl.setPrefSize(200, 100);
        //ctrl.setMaxSize(200, 100);
        Label start = new Label("Start");
        start.setTextFill(Color.WHITE);
        start.setFont(new Font("Helvetica", 28));
        start.setAlignment(Pos.CENTER_LEFT);
        start.setContentDisplay(ContentDisplay.CENTER);
        start.setTranslateX(XTRANS+30*16+100);
        start.setTranslateY(screen.getHeight()/2);
        //start.setTranslateX(-400);
        Circle ico = new Circle(15);
        ico.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent e) {
                FadeTransition ft = new FadeTransition(Duration.millis(500), ctrl);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                FadeTransition tft = new FadeTransition(Duration.millis(500), btitle);
                tft.setFromValue(1.0);
                tft.setToValue(0.0);
                ParallelTransition pt = new ParallelTransition(ft, tft);
                //TranslateTransition fft = new TranslateTransition(Duration.millis(3000), border);
                //tt.setByY(2000);
                SequentialTransition st = new SequentialTransition(pt, infinite);
                st.setOnFinished(new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent e) {
                        State state = State.state().rules(rules2).random(new Rand()).size(400);
                        Iterator<Rule> it = state.rules().random(state.random().create());
                        CA ca = new CA(it.next(), new RandomInitializer(), state.random().create(), 0, state.size(), state.size());
                        state.ca(ca);
                        //final Futures futures = new Futures(rules2, new Rand());
                        final Controls controls = new Controls(state);
                        //controls.setTranslateX(screen.getWidth()/2 - futures.getPossibilityWidth()/2);
                        //controls.setTranslateY(screen.getHeight()/2 - futures.getPossiblityHeight()/2-20);

                        //controls.setTranslateX(screen.getWidth()/2 - (3*200+2*10)/2);
                        //controls.setTranslateY(screen.getHeight()/2 - (3*200+2*10)/2-20);

                        for(Starfield sf:stars) {
                            state.addListener(sf);
                            //futures.addFutureListener(sf);
                         }
                        //futures.addFutureListener(starfield1);
                        //futures.addFutureListener(starfield2);
                        border.getChildren().remove(grid);
                        border.getChildren().remove(btitle);
                        //border.getChildren().add(futures);
                        border.getChildren().add(controls);
                        //futures.setTranslateX(screen.getWidth()/2 - futures.getPossibilityWidth()/2);
                        //futures.setTranslateY(screen.getHeight()/2 - futures.getPossiblityHeight()/2);
                        //border.setCenter(futures);
                        //border.setAlignment(futures, Pos.CENTER);
                    }
                });
                st.play();
            }
        });
        //Sphere ico = new Sphere(15);
        //ico.setDrawMode(DrawMode.LINE);
        ico.setFill(Color.rgb(10,10,10));
        ico.setStroke(Color.WHITE);
        ico.setStrokeWidth(3);
        ico.setTranslateX(XTRANS+30*16+100);
        ico.setTranslateY(screen.getHeight()/2);
        //ctrl.setTop(ico);
        ctrl.setCenter(ico);
        /*
        border.setRight(ctrl);
 
        border.setMaxSize(800,600);
        border.setPrefSize(800,600);
        */
        border.getChildren().add(ctrl);
        Group root = new Group();
        root.getChildren().add(border);
        //root.setAutoSizeChildren(false);
        //root.setLayoutX(-400);
        //root.setLayoutY(-400);
        //Scene scene = new Scene(root, 1200, 1000);
        Scene scene = new Scene(root, 1280, 1024, true, SceneAntialiasing.DISABLED);
        scene.setFill(Color.BLACK);
        scene.setCamera(new PerspectiveCamera());

        //set Stage boundaries to visible bounds of the main screen
        stage.setX(screen.getMinX());
        stage.setY(screen.getMinY());
        stage.setWidth(screen.getWidth());
        stage.setHeight(screen.getHeight());

        stage.setTitle("Explorations in Cellular Spaces");
        stage.setScene(scene);
        stage.setResizable(false);
        //root.autosize();
        //stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}

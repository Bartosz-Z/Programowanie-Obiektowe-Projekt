package agh.ics.oop.gui;

import agh.ics.oop.maps.JungleMap;
import agh.ics.oop.maps.WrappedJungleMap;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        ResourcesLoader resourcesLoader = new ResourcesLoader();
        WorldMapGUI jungleMapGUI = new WorldMapGUI(JungleMap.class, resourcesLoader);
        WorldMapGUI wrappedJungleMapGUI = new WorldMapGUI(WrappedJungleMap.class, resourcesLoader);

        HBox maps = new HBox(wrappedJungleMapGUI.createGUI(), jungleMapGUI.createGUI());
        maps.setMinWidth(500);
        maps.setSpacing(30);
        maps.setAlignment(Pos.CENTER);

        Scene scene = new Scene(maps, 700, 500, Color.BROWN);

        primaryStage.setOnCloseRequest(event -> {
            jungleMapGUI.killSimulation();
            wrappedJungleMapGUI.killSimulation();
        });

        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

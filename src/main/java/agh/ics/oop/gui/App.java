package agh.ics.oop.gui;

import agh.ics.oop.maps.AbstractJungleMap;
import agh.ics.oop.maps.JungleMap;
import agh.ics.oop.SimulationEngine;
import agh.ics.oop.maps.WrappedJungleMap;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        int mapWidth = 20;
        int mapHeight = 20;

        AbstractJungleMap jungleMap = new JungleMap(mapWidth, mapHeight, 0.5f);
        AbstractJungleMap wrappedJungleMap = new WrappedJungleMap(mapWidth, mapHeight, 0.5f);

        ResourcesLoader resourcesLoader = new ResourcesLoader();
        ResourcesLoader resourcesLoader2 = new ResourcesLoader();
        WorldMapRenderer jungleWorldMapRenderer = new WorldMapRenderer(jungleMap, resourcesLoader);
        WorldMapRenderer wrappedJungleWorldMapRenderer = new WorldMapRenderer(wrappedJungleMap, resourcesLoader2);

        GridPane jungleGrid = jungleWorldMapRenderer.createGrid(8);
        GridPane wrappedJungleGrid = wrappedJungleWorldMapRenderer.createGrid(8);

        jungleMap.addObserver(jungleWorldMapRenderer);
        wrappedJungleMap.addObserver(wrappedJungleWorldMapRenderer);

        SimulationEngine jungleEngine = new SimulationEngine(
                jungleMap, 20, 400, 400, 300, false);
        SimulationEngine wrappedJungleEngine = new SimulationEngine(
                wrappedJungleMap, 20, 400, 400, 300, false);
        jungleEngine.addObserver(jungleWorldMapRenderer);
        wrappedJungleEngine.addObserver(wrappedJungleWorldMapRenderer);

        HBox maps = new HBox(jungleGrid, wrappedJungleGrid);
        maps.setAlignment(Pos.CENTER);

        Scene scene = new Scene(maps, 400, 400, Color.BROWN);

        primaryStage.setScene(scene);
        primaryStage.show();

        Thread jungleEngineThread = new Thread(jungleEngine);
        Thread wrappedJungleEngineThread = new Thread(wrappedJungleEngine);

        jungleEngineThread.start();
        wrappedJungleEngineThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

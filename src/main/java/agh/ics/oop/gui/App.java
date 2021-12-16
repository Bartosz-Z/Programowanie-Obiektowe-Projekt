package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        int mapWidth = 20;
        int mapHeight = 20;

        AbstractJungleMap map = new JungleMap(mapWidth, mapHeight, 0.4f);
        System.out.println("Map Done");

        ResourcesLoader resourcesLoader = new ResourcesLoader();
        System.out.println("Resource loader Done");
        WorldMapRenderer worldMapRenderer = new WorldMapRenderer(map, resourcesLoader);
        System.out.println("World map renderer Done");

        GridPane grid = worldMapRenderer.createGrid(16);
        System.out.println("Grid Done");

        map.addObserver(worldMapRenderer);

        SimulationEngine engine = new SimulationEngine(
                map, 20, 100, 200, 50, false);
        engine.addObserver(worldMapRenderer);
        System.out.println("Engine Done");

        Scene scene = new Scene(grid, 400, 400, Color.BROWN);

        primaryStage.setScene(scene);
        primaryStage.show();

        Thread engineThread = new Thread(engine);

        engineThread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

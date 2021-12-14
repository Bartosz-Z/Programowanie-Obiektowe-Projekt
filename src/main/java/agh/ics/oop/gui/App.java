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
        int mapWidth = 10;
        int mapHeight = 5;

        AbstractJungleMap map = new JungleMap(mapWidth, mapHeight, 0.6f);

        ResourcesLoader resourcesLoader = new ResourcesLoader();
        WorldMapRenderer worldMapRenderer = new WorldMapRenderer(map, resourcesLoader);
        GridPane grid = worldMapRenderer.createGrid(30);

        map.addObserver(worldMapRenderer);

        SimulationEngine engine = new SimulationEngine(
                map, 10, 100, 200, 50);

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

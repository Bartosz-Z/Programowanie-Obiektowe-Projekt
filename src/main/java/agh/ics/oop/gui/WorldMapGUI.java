package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import agh.ics.oop.maps.AbstractJungleMap;
import agh.ics.oop.observers.IOnTileClickedEventObserver;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.ObjectsFactory;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;


public class WorldMapGUI implements IOnTileClickedEventObserver {
    private final Class<? extends AbstractJungleMap> mapClass;
    private final ResourcesLoader resourcesLoader;
    private final ObjectsFactory objectsFactory;

    private final TextField
            mapSizeXTextField = new TextField("10"),
            mapSizeYTextField = new TextField("10"),
            animalEnergyTextField = new TextField("200"),
            animalMaxEnergyTextField = new TextField("400"),
            animalForwardCostTextField = new TextField("10"),
            animalBackwardCostTextField = new TextField("15"),
            animalRotationCostTextField = new TextField("5"),
            animalsCountTextField = new TextField("5"),
            grassEnergyTextField = new TextField("300"),
            jungleRatioTextField = new TextField("0.6"),
            mapTileSize = new TextField("16"),
            simulationTimeDelayTextField = new TextField("300");
    private Label selectedAnimalGenomeLabel;


    private final RadioButton isMagicEvolutionAllowed = new RadioButton("Is magic evolution allowed?");

    private ScrollPane scrollPane;

    private AbstractJungleMap map;
    private SimulationEngine engine;
    private GridPane mapGrid;
    private WorldMapRenderer renderer;

    private List<Vector2d> highlightedPositions;

    public WorldMapGUI(Class<? extends AbstractJungleMap> mapClass, ResourcesLoader resourcesLoader) {
        objectsFactory = new ObjectsFactory();
        this.mapClass = mapClass;
        this.resourcesLoader = resourcesLoader;
    }

    private Node createInputGUI(String labelText, TextField textField) {
        Label label = new Label(labelText);
        textField.setMaxWidth(100);
        VBox vBox = new VBox(label, textField);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    public Parent createGUI() {
        scrollPane = new ScrollPane();
        scrollPane.setContent(generateMenu());
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    private Node generateMenu() {
        // Map size
        Label mapSizeLabel = new Label("Enter map size");
        mapSizeXTextField.setMaxWidth(50);
        mapSizeYTextField.setMaxWidth(50);
        HBox mapSizeTextFields = new HBox(mapSizeXTextField, new Label(" x "), mapSizeYTextField);
        mapSizeTextFields.setAlignment(Pos.CENTER);
        VBox mapSize = new VBox(mapSizeLabel, mapSizeTextFields);
        mapSize.setAlignment(Pos.CENTER);

        // Button to generate map
        Button generateMapButton = new Button("Generate Map");
        generateMapButton.setOnAction(event -> generateMap());

        VBox menu = new VBox(
                mapSize,
                createInputGUI("Enter animal's start energy", animalEnergyTextField),
                createInputGUI("Enter animal's maximum energy", animalMaxEnergyTextField),
                createInputGUI("Enter animal's moving forward cost", animalForwardCostTextField),
                createInputGUI("Enter animal's moving backward cost", animalBackwardCostTextField),
                createInputGUI("Enter animal's rotation cost", animalRotationCostTextField),
                createInputGUI("Enter animals number", animalsCountTextField),
                createInputGUI("Enter grass's energy", grassEnergyTextField),
                createInputGUI("Enter jungle ratio", jungleRatioTextField),
                createInputGUI("Enter tile size", mapTileSize),
                createInputGUI("Enter time delay between epochs", simulationTimeDelayTextField),
                isMagicEvolutionAllowed,
                generateMapButton
        );
        menu.setAlignment(Pos.CENTER);

        return menu;
    }

    private void generateMap() {
        try {
            map = objectsFactory.createObject(mapClass,
                    Integer.parseInt(mapSizeXTextField.getText()),
                    Integer.parseInt(mapSizeYTextField.getText()),
                    Float.parseFloat(jungleRatioTextField.getText()));

            renderer = new WorldMapRenderer(map, resourcesLoader);
            renderer.addObserver(this);
            mapGrid = renderer.createGrid(Integer.parseInt(mapTileSize.getText()));

            map.addObserver(renderer);

            engine = new SimulationEngine(
                    map,
                    Integer.parseInt(animalsCountTextField.getText()),
                    Integer.parseInt(animalEnergyTextField.getText()),
                    Integer.parseInt(animalMaxEnergyTextField.getText()),
                    Integer.parseInt(animalForwardCostTextField.getText()),
                    Integer.parseInt(animalBackwardCostTextField.getText()),
                    Integer.parseInt(animalRotationCostTextField.getText()),
                    Integer.parseInt(grassEnergyTextField.getText()),
                    Integer.parseInt(simulationTimeDelayTextField.getText()),
                    isMagicEvolutionAllowed.isSelected()
            );
            engine.addObserver(renderer);
        } catch (Exception e) {
            new Alert(Alert.AlertType.INFORMATION, "You entered invalid parameters.").showAndWait();
            return;
        }

        StatisticsModule statisticsModule = new StatisticsModule();
        engine.addObserver(statisticsModule);

        Button stopSimulationButton = new Button("Stop simulation");

        Button resumeSimulationButton = new Button("Resume simulation");
        resumeSimulationButton.setVisible(false);
        resumeSimulationButton.setManaged(false);

        Button saveSimulationButton = new Button("Save Statistics to file");
        saveSimulationButton.setVisible(false);

        Button highlightDominantGenomesButton = new Button("Highlight dominant genomes");
        highlightDominantGenomesButton.setVisible(false);

        stopSimulationButton.setOnAction(event -> {
            engine.stopSimulation();
            stopSimulationButton.setVisible(false);
            stopSimulationButton.setManaged(false);
            saveSimulationButton.setVisible(true);
            highlightDominantGenomesButton.setVisible(true);
            resumeSimulationButton.setVisible(true);
            resumeSimulationButton.setManaged(true);
        });
        resumeSimulationButton.setOnAction(event -> {
            engine.resumeSimulation();
            hideDominantGenomeAnimals();
            resumeSimulationButton.setVisible(false);
            resumeSimulationButton.setManaged(false);
            saveSimulationButton.setVisible(false);
            highlightDominantGenomesButton.setVisible(false);
            stopSimulationButton.setVisible(true);
            stopSimulationButton.setManaged(true);
        });
        saveSimulationButton.setOnAction(event -> statisticsModule.saveToFile(mapClass.getName()));
        highlightDominantGenomesButton.setOnAction(event -> showDominantGenomeAnimals());

        Button killSimulation = new Button("End simulation");

        killSimulation.setOnAction(event -> {
            engine.killSimulation();
            scrollPane.setContent(generateMenu());
        });


        HBox mapControlButtons = new HBox(stopSimulationButton, resumeSimulationButton, killSimulation);
        mapControlButtons.setAlignment(Pos.CENTER);
        mapControlButtons.setSpacing(20);

        VBox mapButtons = new VBox(mapControlButtons, saveSimulationButton, highlightDominantGenomesButton);
        mapButtons.setAlignment(Pos.CENTER);

        selectedAnimalGenomeLabel = new Label("");

        VBox mapRoot = new VBox(mapGrid, mapButtons, selectedAnimalGenomeLabel);
        mapRoot.setAlignment(Pos.CENTER);

        HBox root = new HBox(statisticsModule.generateCharts(), mapRoot);
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        scrollPane.setContent(root);

        Thread engineThread = new Thread(engine);
        engineThread.start();
    }

    private Group getCellFromMapGrid(int col, int row) {
        if (mapGrid == null)
            return null;

        for (Node node : mapGrid.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return (Group) node;
            }
        }
        return null;
    }

    private void showDominantGenomeAnimals() {
        hideDominantGenomeAnimals();
        if (mapGrid == null || engine == null || renderer == null || map == null)
            return;

        highlightedPositions = engine.getAllDominantGenomesPositions();

        for (Vector2d position : highlightedPositions) {
            Group cell = getCellFromMapGrid(position.x(), map.size.y() - position.y() - 1);
            if (cell != null) {
                ImageView highlightation = new ImageView(resourcesLoader.getImageOf(ImageName.TILE_HIGHLIGHTATION));
                highlightation.setFitWidth(renderer.getTileSize());
                highlightation.setPreserveRatio(true);
                highlightation.setBlendMode(BlendMode.SRC_OVER);
                cell.getChildren().add(highlightation);
            }
        }
    }

    private void hideDominantGenomeAnimals() {
        if (mapGrid == null || engine == null || renderer == null || map == null || highlightedPositions == null)
            return;

        for (Vector2d position : highlightedPositions) {
            Group cell = getCellFromMapGrid(position.x(), map.size.y() - position.y() - 1);
            if (cell != null) {
                ObservableList<Node> children = cell.getChildren();
                if (children.size() > 2)
                    children.remove(2);
                else
                    break;
            }
        }
    }

    public void killSimulation() {
        if (engine != null)
            engine.killSimulation();
    }

    @Override
    public void onClicked(String event) {
        if (selectedAnimalGenomeLabel != null) {
            if (event.isEmpty())
                selectedAnimalGenomeLabel.setText("");
            else
                selectedAnimalGenomeLabel.setText("Selected genome" + System.lineSeparator() + event);
        }
    }
}

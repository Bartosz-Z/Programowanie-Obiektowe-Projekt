package agh.ics.oop.gui;

import agh.ics.oop.elements.AbstractWorldMapDynamicElement;
import agh.ics.oop.elements.AbstractWorldMapElement;
import agh.ics.oop.elements.Animal;
import agh.ics.oop.maps.AbstractWorldMap;
import agh.ics.oop.observers.*;
import agh.ics.oop.structures.Vector2d;
import agh.ics.oop.utility.Ensure;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.*;

public class WorldMapRenderer
        implements
        IPositionChangeObserver,
        IDirectionChangeObserver,
        IOnDestroyInvokeObserver,
        IOnPlaceElementInvokeObserver,
        IOnEpochEndInvokeObserver,
        IOnTileClickedObservable {
    private final AbstractWorldMap map;
    private final ResourcesLoader resourcesLoader;
    private GridPane grid;
    private int tileSize;
    private final ImageView[][] elementsImage;
    private final Map<Vector2d, ImageName> tilesToUpdate;

    private final List<IOnTileClickedEventObserver> onClickedObservers;

    public WorldMapRenderer (AbstractWorldMap map, ResourcesLoader resourcesLoader) {
        Ensure.Not.Null(map, "world map");
        Ensure.Not.Null(resourcesLoader, "resource loader");

        this.map = map;
        this.resourcesLoader = resourcesLoader;
        elementsImage = new ImageView[map.size.y()][map.size.x()];
        tilesToUpdate = new HashMap<>();
        onClickedObservers = new LinkedList<>();
    }

    public GridPane createGrid(int tileSize) {
        this.tileSize = tileSize;
        grid = new GridPane();

        grid.setAlignment(Pos.CENTER);

        for (int i = 0; i < map.size.x(); i++)
            grid.getColumnConstraints().add(new ColumnConstraints(tileSize));
        for (int i = 0; i < map.size.y(); i++)
            grid.getRowConstraints().add(new RowConstraints(tileSize));

        List<Node> nodesToAdd = new LinkedList<>();

        for (int row = 0; row < grid.getRowCount(); row++)
            for (int col = 0; col < grid.getColumnCount(); col++) {
                Vector2d tilePosition = new Vector2d(col, map.size.y() - row - 1);

                ImageView tileGround = new ImageView(resourcesLoader.getImageOf(map.getImageNameOfTile(tilePosition)));
                ImageView tileElement = new ImageView();

                tileGround.setFitWidth(tileSize);
                tileGround.setPreserveRatio(true);
                tileElement.setFitWidth(tileSize);
                tileElement.setPreserveRatio(true);
                tileElement.setBlendMode(BlendMode.SRC_OVER);

                tileElement.setOnMouseClicked(event -> {
                    if (map.objectAt(tilePosition) instanceof Animal animal)
                        onClickedObservers.forEach(observer -> Platform.runLater(() ->
                                observer.onClicked(animal.getGenome().toString())));
                    else
                        onClickedObservers.forEach(observer -> Platform.runLater(() ->
                                observer.onClicked("")));
                });

                elementsImage[tilePosition.y()][tilePosition.x()] = tileElement;

                Group tileImage = new Group(tileGround, tileElement);

                GridPane.setConstraints(tileImage, col, row);
                nodesToAdd.add(tileImage);
            }

        grid.getChildren().addAll(nodesToAdd);

        return grid;
    }

    private void updatePosition(Vector2d position) {
        Ensure.Not.Null(position, "update position");

        if (position.follows(Vector2d.zero) &&position.precedes(new Vector2d(map.size.x() - 1, map.size.y() - 1))) {
            AbstractWorldMapElement elementOnTile = map.objectAt(position);

            if (elementOnTile == null)
                tilesToUpdate.put(position, ImageName.TILE_BLANK);
            else
                tilesToUpdate.put(position, elementOnTile.getImageName());
        }
    }

    @Override
    public void directionChanged(AbstractWorldMapDynamicElement element) {
        Ensure.Not.Null(element, "element");

        updatePosition(element.getPosition());
    }

    @Override
    public void positionChanged(AbstractWorldMapDynamicElement element, Vector2d oldPosition, Vector2d newPosition) {
        updatePosition(oldPosition);
        updatePosition(newPosition);
    }

    @Override
    public void onElementDestroy(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element");

        updatePosition(element.getPosition());
    }

    @Override
    public void elementPlaced(AbstractWorldMapElement element) {
        Ensure.Not.Null(element, "element");

        if (element instanceof IPositionObservable)
            ((IPositionObservable) element).addObserver(this);
        if (element instanceof IDirectionObservable)
            ((IDirectionObservable) element).addObserver(this);
        element.addObserver(this);

        updatePosition(element.getPosition());
    }

    @Override
    public void epochEnd() {
        List<Map.Entry<Vector2d, ImageName>> tiles = new ArrayList<>(tilesToUpdate.entrySet());
        tilesToUpdate.clear();
        Platform.runLater(() -> {
            for (Map.Entry<Vector2d, ImageName> tile : tiles)
                elementsImage[tile.getKey().y()][tile.getKey().x()]
                        .setImage(resourcesLoader.getImageOf(tile.getValue()));
        });
    }

    public int getTileSize() {
        return tileSize;
    }

    @Override
    public void addObserver(IOnTileClickedEventObserver observer) {
        onClickedObservers.add(observer);
    }

    @Override
    public void removeObserver(IOnTileClickedEventObserver observer) {
        onClickedObservers.remove(observer);
    }
}

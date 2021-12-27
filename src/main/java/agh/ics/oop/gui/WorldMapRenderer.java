package agh.ics.oop.gui;

import agh.ics.oop.*;
import agh.ics.oop.observers.*;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Group;
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
        IOnEpochEndInvokeObserver {
    private final AbstractWorldMap map;
    private final ResourcesLoader resourcesLoader;
    private GridPane grid;
    private final ImageView[][] elementsImage;
    private final Map<Vector2d, ImageName> tilesToUpdate;

    public WorldMapRenderer (AbstractWorldMap map, ResourcesLoader resourcesLoader) {
        Ensure.Not.Null(map, "world map");
        Ensure.Not.Null(resourcesLoader, "resource loader");

        this.map = map;
        this.resourcesLoader = resourcesLoader;
        elementsImage = new ImageView[map.size.y()][map.size.x()];
        tilesToUpdate = new HashMap<>();
    }

    public GridPane createGrid(int fieldSize) {
        grid = new GridPane();

        grid.setAlignment(Pos.CENTER);
        grid.setGridLinesVisible(true);

        for (int i = 0; i < map.size.x(); i++)
            grid.getColumnConstraints().add(new ColumnConstraints(fieldSize));
        for (int i = 0; i < map.size.y(); i++)
            grid.getRowConstraints().add(new RowConstraints(fieldSize));

        for (int row = 0; row < grid.getRowCount(); row++)
            for (int col = 0; col < grid.getColumnCount(); col++) {
                Vector2d tilePosition = new Vector2d(col, map.size.y() - row - 1);

                ImageView tileGround = new ImageView(resourcesLoader.getImageOf(map.getImageNameOfTile(tilePosition)));
                ImageView tileElement = new ImageView();

                tileGround.setFitWidth(fieldSize);
                tileGround.setPreserveRatio(true);
                tileElement.setFitWidth(fieldSize);
                tileElement.setPreserveRatio(true);
                tileElement.setBlendMode(BlendMode.SRC_OVER);

                elementsImage[tilePosition.y()][tilePosition.x()] = tileElement;

                Group tileImage = new Group(tileGround, tileElement);

                GridPane.setConstraints(tileImage, col, row);
                grid.getChildren().add(tileImage);
            }

        return grid;
    }

    private void updatePosition(Vector2d position) {
        Ensure.Not.Null(position, "update position");

        AbstractWorldMapElement elementOnTile = map.firstObjectAt(position);

        if (elementOnTile == null)
            tilesToUpdate.put(position, ImageName.TILE_BLANK);
        else
            tilesToUpdate.put(position, elementOnTile.getImageName());
    }

    @Override
    public void directionChanged(AbstractWorldMapDynamicElement element) {
        Ensure.Not.Null(element, "element");

        updatePosition(element.getPosition());
    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {
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
}

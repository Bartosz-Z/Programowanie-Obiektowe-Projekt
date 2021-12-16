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

import java.util.HashSet;
import java.util.Set;

public class WorldMapRenderer
        implements
        IPositionChangeObserver,
        IDirectionChangeObserver,
        IOnDestroyInvokeObserver,
        IOnPlaceElementInvokeObserver,
        IOnNewEpochInvokeObserver {
    private final AbstractWorldMap map;
    private final ResourcesLoader resourcesLoader;
    private GridPane grid;
    private final ImageView[][] elementsImage;
    private final Set<Vector2d> tilesToUpdate;

    public WorldMapRenderer (AbstractWorldMap map, ResourcesLoader resourcesLoader) {
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null.");
        if (resourcesLoader == null)
            throw new IllegalArgumentException("'resourcesLoader' argument can not be null.");

        this.map = map;
        this.resourcesLoader = resourcesLoader;
        elementsImage = new ImageView[map.size.y()][map.size.x()];
        tilesToUpdate = new HashSet<>();
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
        if (position == null)
            throw new IllegalArgumentException("'position' argument can not be null.");
        tilesToUpdate.add(position);
    }

    @Override
    public void directionChanged(AbstractWorldMapDynamicElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        updatePosition(element.getPosition());
    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        updatePosition(oldPosition);
        updatePosition(newPosition);
    }

    @Override
    public void onElementDestroy(AbstractWorldMapDynamicElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        updatePosition(element.getPosition());
    }

    @Override
    public void elementPlaced(AbstractWorldMapElement element) {
        if (element == null)
            throw new IllegalArgumentException("'element' argument can not be null.");

        if (element instanceof IPositionObservable)
            ((IPositionObservable) element).addObserver(this);
        if (element instanceof IDirectionObservable)
            ((IDirectionObservable) element).addObserver(this);
        if (element instanceof IOnDestroyObservable)
            ((IOnDestroyObservable) element).addObserver(this);

        updatePosition(element.getPosition());
    }

    @Override
    public void newEpoch() {
        Vector2d[] positions = tilesToUpdate.toArray(Vector2d[]::new);
        tilesToUpdate.clear();
        Platform.runLater(() -> {
            for (Vector2d position : positions) {
                AbstractWorldMapElement elementOnPosition = map.firstObjectAt(position);
                if (elementOnPosition == null)
                    elementsImage[position.y()][position.x()].setImage(resourcesLoader.getImageOf(ImageName.TILE_BLANK));
                else
                    elementsImage[position.y()][position.x()].setImage(
                        resourcesLoader.getImageOf(elementOnPosition.getImageName()));
            }
        });
    }
}

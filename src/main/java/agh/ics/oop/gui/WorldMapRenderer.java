package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.effect.BlendMode;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

public class WorldMapRenderer implements IPositionChangeObserver, IDirectionChangeObserver {
    private final AbstractWorldMap map;
    private final ResourcesLoader resourcesLoader;
    private GridPane grid;
    private final ImageView[][] elementsImage;

    public WorldMapRenderer (AbstractWorldMap map, ResourcesLoader resourcesLoader) {
        if (map == null)
            throw new IllegalArgumentException("'map' argument can not be null.");
        if (resourcesLoader == null)
            throw new IllegalArgumentException("'resourcesLoader' argument can not be null.");

        this.map = map;
        this.resourcesLoader = resourcesLoader;
        elementsImage = new ImageView[map.size.y()][map.size.x()];
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
                AbstractWorldMapElement element = map.objectAt(tilePosition);

                ImageView tileGround = new ImageView(resourcesLoader.getImageOf(map.getImageNameOfTile(tilePosition)));
                ImageView tileElement;

                if (element == null)
                    tileElement = new ImageView(resourcesLoader.getImageOf(ImageName.TILE_BLANK));
                else {
                    tileElement = new ImageView(resourcesLoader.getImageOf(element.getImageName()));

                    if (element instanceof IPositionObservable)
                        ((IPositionObservable)element).addObserver(this);
                    if (element instanceof IDirectionObservable)
                        ((IDirectionObservable)element).addObserver(this);
                }

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

    @Override
    public void directionChanged(AbstractWorldMapDynamicElement element) {
        Vector2d elementPosition = element.getPosition();
        elementsImage[elementPosition.y()][elementPosition.x()].setImage(resourcesLoader.getImageOf(element.getImageName()));
    }

    @Override
    public void positionChanged(AbstractWorldMapElement element, Vector2d oldPosition, Vector2d newPosition) {
        AbstractWorldMapElement oldPositionElement = map.objectAt(oldPosition);
        AbstractWorldMapElement newPositionElement = map.objectAt(newPosition);
        if (oldPositionElement == null)
            elementsImage[oldPosition.y()][oldPosition.x()].setImage(resourcesLoader.getImageOf(ImageName.TILE_BLANK));
        else
            elementsImage[oldPosition.y()][oldPosition.x()].setImage(resourcesLoader.getImageOf(oldPositionElement.getImageName()));
        if (newPositionElement == null)
            elementsImage[newPosition.y()][newPosition.x()].setImage(resourcesLoader.getImageOf(ImageName.TILE_BLANK));
        else
            elementsImage[newPosition.y()][newPosition.x()].setImage(resourcesLoader.getImageOf(newPositionElement.getImageName()));
    }
}

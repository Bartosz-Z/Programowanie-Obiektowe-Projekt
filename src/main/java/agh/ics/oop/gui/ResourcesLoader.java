package agh.ics.oop.gui;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.EnumMap;

public class ResourcesLoader {
    private final EnumMap<ImageName, Image> images;

    public ResourcesLoader() throws FileNotFoundException {
        images = new EnumMap<>(ImageName.class);

        for (ImageName fileName : ImageName.values())
            images.put(fileName, loadImage(fileName.name));
        images.put(ImageName.TILE_BLANK, null);
    }

    public Image getImageOf(ImageName fileName) {
        return images.get(fileName);
    }

    private Image loadImage(String imageName) throws FileNotFoundException {
        return new Image(new FileInputStream("src\\main\\resources\\" + imageName));
    }
}

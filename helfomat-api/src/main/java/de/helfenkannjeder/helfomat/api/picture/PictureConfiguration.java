package de.helfenkannjeder.helfomat.api.picture;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Valentin Zickner
 */
@ConfigurationProperties("helfomat.picture")
public class PictureConfiguration {

    private static final String DEFAULT_PICTURE_FOLDER = "pictures";

    private String pictureFolder = DEFAULT_PICTURE_FOLDER;

    private List<PictureSize> pictureSizes = new ArrayList<>();

    public String getPictureFolder() {
        return pictureFolder;
    }

    public void setPictureFolder(String pictureFolder) {
        this.pictureFolder = pictureFolder;
    }

    public List<PictureSize> getPictureSizes() {
        return pictureSizes;
    }

    public void setPictureSizes(List<PictureSize> pictureSizes) {
        this.pictureSizes = pictureSizes;
    }

    public static class PictureSize {
        private String name;
        private Integer width;
        private Integer height;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }
    }
}

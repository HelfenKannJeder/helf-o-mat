package de.helfenkannjeder.helfomat.service;

import de.helfenkannjeder.helfomat.domain.PictureId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Valentin Zickner
 */
@Service
public class PictureService {

    @Value("${helfomat.pictureFolder}")
    private String pictureFolder;

    public Path getPicture(PictureId pictureId) throws IOException {
        return Paths.get(pictureFolder, "helfomat", pictureId.getValue());
    }
}

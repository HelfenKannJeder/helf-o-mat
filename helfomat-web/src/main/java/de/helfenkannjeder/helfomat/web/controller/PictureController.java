package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.core.organisation.PictureId;
import de.helfenkannjeder.helfomat.core.picture.PictureRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping("/api")
public class PictureController {

    private final PictureRepository pictureRepository;

    public PictureController(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    @GetMapping("/picture/{pictureId}/{size}")
    public ResponseEntity<InputStreamResource> downloadPicture(@PathVariable PictureId pictureId, @PathVariable String size) throws IOException {
        Path picture;
        if (size == null) {
            picture = pictureRepository.getPicture(pictureId);
        } else {
            picture = pictureRepository.getPicture(pictureId, size);
        }

        if (!Files.exists(picture)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(Files.probeContentType(picture)));
        InputStream inputStream = Files.newInputStream(picture);
        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

}

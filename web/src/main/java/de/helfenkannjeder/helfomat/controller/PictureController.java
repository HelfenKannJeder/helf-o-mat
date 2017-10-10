package de.helfenkannjeder.helfomat.controller;

import de.helfenkannjeder.helfomat.domain.PictureId;
import de.helfenkannjeder.helfomat.service.PictureService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Valentin Zickner
 */
@RestController
public class PictureController {

    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping("/picture/{pictureId}")
    public ResponseEntity<InputStreamResource> downloadPicture(@PathVariable("pictureId") PictureId pictureId) throws IOException {
        Path picture = pictureService.getPicture(pictureId);
        if (!Files.exists(picture)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(Files.probeContentType(picture)));
        InputStream inputStream = Files.newInputStream(picture);
        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

}

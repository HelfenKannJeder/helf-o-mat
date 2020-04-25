package de.helfenkannjeder.helfomat.web.controller;

import de.helfenkannjeder.helfomat.api.picture.PictureApplicationService;
import de.helfenkannjeder.helfomat.core.picture.DownloadFailedException;
import de.helfenkannjeder.helfomat.core.picture.PictureId;
import org.apache.tika.Tika;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    private static final Tika TIKA = new Tika();

    private final PictureApplicationService pictureApplicationService;

    public PictureController(PictureApplicationService pictureApplicationService) {
        this.pictureApplicationService = pictureApplicationService;
    }

    @GetMapping("/picture/{pictureId}")
    public ResponseEntity<InputStreamResource> downloadPicture(@PathVariable PictureId pictureId) throws IOException {
        Path picture = this.pictureApplicationService.getPicture(pictureId);
        return toResponseEntity(picture);
    }

    @GetMapping("/picture/{pictureId}/{size}")
    public ResponseEntity<InputStreamResource> downloadPicture(@PathVariable PictureId pictureId, @PathVariable String size) throws IOException {
        Path picture = this.pictureApplicationService.getPicture(pictureId, size);
        return toResponseEntity(picture);
    }

    @PostMapping("/picture/{pictureId}")
    public void savePicture(@PathVariable PictureId pictureId, @RequestParam("file") MultipartFile file) throws IOException, DownloadFailedException {
        this.pictureApplicationService.savePicture(pictureId, file.getInputStream());
    }

    private ResponseEntity<InputStreamResource> toResponseEntity(Path picture) throws IOException {
        if (!Files.exists(picture)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(TIKA.detect(picture)));
        InputStream inputStream = Files.newInputStream(picture);
        return new ResponseEntity<>(new InputStreamResource(inputStream), headers, HttpStatus.OK);
    }

}

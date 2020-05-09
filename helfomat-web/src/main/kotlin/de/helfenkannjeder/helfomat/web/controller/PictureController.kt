package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.picture.PictureApplicationService
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.apache.tika.Tika
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path

/**
 * @author Valentin Zickner
 */
@RestController
@RequestMapping("/api")
class PictureController(private val pictureApplicationService: PictureApplicationService) {

    private val TIKA = Tika()

    @GetMapping("/picture/{pictureId}")
    fun downloadPicture(@PathVariable pictureId: PictureId) =
        toResponseEntity(pictureApplicationService.getPicture(pictureId))

    @GetMapping("/picture/{pictureId}/{size}")
    fun downloadPicture(@PathVariable pictureId: PictureId, @PathVariable size: String) =
        toResponseEntity(pictureApplicationService.getPicture(pictureId, size))

    @PostMapping("/picture/{pictureId}")
    fun savePicture(@PathVariable pictureId: PictureId, @RequestParam("file") file: MultipartFile) =
        pictureApplicationService.savePicture(pictureId, file.inputStream, file.contentType)

    private fun toResponseEntity(picture: Path): ResponseEntity<InputStreamResource> {
        if (!Files.exists(picture)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        val headers = HttpHeaders()
        headers.contentType = MediaType.parseMediaType(TIKA.detect(picture))
        val inputStream = Files.newInputStream(picture)
        return ResponseEntity(InputStreamResource(inputStream), headers, HttpStatus.OK)
    }

}
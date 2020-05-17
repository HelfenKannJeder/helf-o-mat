package de.helfenkannjeder.helfomat.web.controller

import de.helfenkannjeder.helfomat.api.picture.PictureApplicationService
import de.helfenkannjeder.helfomat.api.picture.PictureDto
import de.helfenkannjeder.helfomat.core.picture.PictureId
import org.apache.tika.Tika
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

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
        pictureApplicationService.savePicture(pictureId, file.inputStream, file.size)

    private fun toResponseEntity(pictureDto: PictureDto): ResponseEntity<InputStreamResource> {
        val headers = HttpHeaders()
        val contentType = pictureDto.contentType
        if (contentType != null) {
            headers.contentType = MediaType.parseMediaType(contentType)
        }
        return ResponseEntity(InputStreamResource(pictureDto.inputStream), headers, HttpStatus.OK)
    }

}
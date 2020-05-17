package de.helfenkannjeder.helfomat.web.exception

import de.helfenkannjeder.helfomat.api.organization.OrganizationNotFoundException
import de.helfenkannjeder.helfomat.api.picture.PictureNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

/**
 * @author Valentin Zickner
 */
@ControllerAdvice
open class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    private val LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler
    open fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorDetails> {
        LOGGER.error("Handle IllegalArgumentException", e)
        return createErrorResponse(HttpStatus.BAD_REQUEST, e)
    }

    @ExceptionHandler
    open fun handleOrganizationNotFoundException(organizationNotFoundException: OrganizationNotFoundException): ResponseEntity<ErrorDetails> {
        return createErrorResponse(HttpStatus.NOT_FOUND, organizationNotFoundException)
    }

    @ExceptionHandler
    open fun handlePictureNotFoundException(pictureNotFoundException: PictureNotFoundException): ResponseEntity<ErrorDetails> =
        createErrorResponse(HttpStatus.NOT_FOUND, pictureNotFoundException)

    private fun createErrorResponse(status: HttpStatus, e: Exception): ResponseEntity<ErrorDetails> {
        return ResponseEntity(ErrorDetails(e.javaClass.simpleName), HttpHeaders(), status)
    }

    class ErrorDetails(val exceptionName: String)

}
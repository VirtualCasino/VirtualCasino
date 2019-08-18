package pl.edu.pollub.virtualcasino

import org.springframework.http.HttpStatus.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(value = [DomainObjectInvalidUsed::class])
    fun handle(exception: DomainObjectInvalidUsed): ResponseEntity<ExceptionView> {
        return ResponseEntity.badRequest().body(ExceptionView(exception.code(), exception.params()))
    }

    @ExceptionHandler(value = [DomainObjectNotExist::class])
    fun handle(exception: DomainObjectNotExist): ResponseEntity<ExceptionView> {
        return ResponseEntity.status(NOT_FOUND).body(ExceptionView(exception.code(), exception.params()))
    }

    @ExceptionHandler(value = [Exception::class])
    fun handle(exception: Exception): ResponseEntity<ExceptionView> {
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(ExceptionView("internalServerError", emptyMap()))
    }
}

data class ExceptionView(val code: String, val params: Map<String, String>)
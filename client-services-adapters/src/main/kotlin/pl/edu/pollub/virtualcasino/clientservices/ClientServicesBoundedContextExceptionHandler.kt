package pl.edu.pollub.virtualcasino.clientservices

import org.springframework.web.bind.annotation.ControllerAdvice
import pl.edu.pollub.virtualcasino.ExceptionHandler

@ControllerAdvice
class ClientServicesBoundedContextExceptionHandler: ExceptionHandler()
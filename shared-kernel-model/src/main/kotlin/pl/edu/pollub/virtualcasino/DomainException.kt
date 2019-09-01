package pl.edu.pollub.virtualcasino

import java.lang.RuntimeException

abstract class DomainException(val code: String, val params: Map<String, String>, message: String): RuntimeException(message) {

    fun code(): String = code

    fun params(): Map<String, String> = params

}

abstract class DomainObjectNotExist(code: String, params: Map<String, String>, message: String): DomainException(code, params, message)

abstract class DomainObjectInvalidUsed(code: String, params: Map<String, String>, message: String): DomainException(code, params, message)


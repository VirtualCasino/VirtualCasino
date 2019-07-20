package pl.edu.pollub.virtualcasino.clientservices.domain

import java.lang.Exception

abstract class DomainException(message: String): Exception(message) {

    abstract fun code(): String

    abstract fun params(): Map<String, String>

}

abstract class DomainObjectNotExist(message: String): DomainException(message)

abstract class DomainObjectInvalidUsed(message: String): DomainException(message)


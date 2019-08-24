package pl.edu.pollub.virtualcasino.clientservices.client.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId

class ClientNotExist(val clientId: ClientId): DomainObjectNotExist("Client with id: ${clientId.value} doesn't exist") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("clientId", clientId.value.toString()))

    companion object {
        const val CODE = "casinoServices.client.clientNotExist"
    }
}
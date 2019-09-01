package pl.edu.pollub.virtualcasino.clientservices.client.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectNotExist
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId

class ClientNotExist(val clientId: ClientId): DomainObjectNotExist(
        CODE,
        mapOf(Pair("clientId", clientId.value.toString())),
        "Client with id: ${clientId.value} doesn't exist"
) {

    companion object {
        const val CODE = "casinoServices.client.clientNotExist"
    }
}
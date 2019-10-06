package pl.edu.pollub.virtualcasino.clientservices.client.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId

class ClientNotRegistered(val clientId: ClientId): DomainObjectInvalidUsed(
        CODE,
        mapOf(Pair("clientId", clientId.value.toString())),
        "Client with id: ${clientId.value} is not registered"
) {

    companion object {
        const val CODE = "casinoServices.client.clientNotRegistered"
    }
}
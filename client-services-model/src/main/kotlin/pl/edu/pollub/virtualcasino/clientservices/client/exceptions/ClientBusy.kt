package pl.edu.pollub.virtualcasino.clientservices.client.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId

class ClientBusy(val clientId: ClientId): DomainObjectInvalidUsed(
        CODE,
        mapOf(Pair("clientId", clientId.value.toString())),
        "Client with id: ${clientId.value} is busy") {

    companion object {
        const val CODE = "casinoServices.client.clientBusy"
    }
}
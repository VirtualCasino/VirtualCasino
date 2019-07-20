package pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId

class ClientBusy(val clientId: ClientId): DomainObjectInvalidUsed("Client with id: ${clientId.value} is busy") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("clientId", clientId.value))

    companion object {
        const val CODE = "client.busy"
    }
}
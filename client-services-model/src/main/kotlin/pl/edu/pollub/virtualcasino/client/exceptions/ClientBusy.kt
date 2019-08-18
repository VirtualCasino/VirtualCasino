package pl.edu.pollub.virtualcasino.client.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.client.ClientId

class ClientBusy(val clientId: ClientId): DomainObjectInvalidUsed("Client with id: ${clientId.value} is busy") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(Pair("clientId", clientId.value.toString()))

    companion object {
        const val CODE = "casinoServices.client.clientBusy"
    }
}
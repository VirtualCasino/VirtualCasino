package pl.edu.pollub.virtualcasino.clientservices.client.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.Nick

class ClientNickNotValid(val nick: Nick): DomainObjectInvalidUsed(
        CODE,
        mapOf(Pair("nick", nick.value)),
        "Client nick: ${nick.value} is not valid"
) {

    companion object {
        const val CODE = "casinoServices.client.clientNickNotValid"
    }
}
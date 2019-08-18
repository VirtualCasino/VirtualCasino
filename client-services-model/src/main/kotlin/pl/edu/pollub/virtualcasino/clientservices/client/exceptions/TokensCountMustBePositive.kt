package pl.edu.pollub.virtualcasino.clientservices.client.exceptions

import pl.edu.pollub.virtualcasino.DomainObjectInvalidUsed
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens

class TokensCountMustBePositive(val clientId: ClientId, val tokens: Tokens):
        DomainObjectInvalidUsed("Client with id: ${clientId.value} can't buy tokens: ${tokens.count} because tokens count must be positive") {

    override fun code(): String = CODE

    override fun params(): Map<String, String> = mapOf(
            Pair("clientId", clientId.value.toString()),
            Pair("tokens", tokens.count.toString())
    )

    companion object {
        const val CODE = "casinoServices.client.tokensCountMustBePositive"
    }

}
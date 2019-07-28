package pl.edu.pollub.virtualcasino.clientservices.domain.client.commands

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens

data class BuyTokens(val clientId: ClientId, val tokens: Tokens = Tokens())
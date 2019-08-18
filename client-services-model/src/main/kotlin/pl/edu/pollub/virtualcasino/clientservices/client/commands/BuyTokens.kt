package pl.edu.pollub.virtualcasino.clientservices.client.commands

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens

data class BuyTokens(val clientId: ClientId, val tokens: Tokens = Tokens())
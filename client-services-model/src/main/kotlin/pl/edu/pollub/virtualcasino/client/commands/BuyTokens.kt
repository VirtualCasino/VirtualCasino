package pl.edu.pollub.virtualcasino.client.commands

import pl.edu.pollub.virtualcasino.client.ClientId
import pl.edu.pollub.virtualcasino.client.Tokens

data class BuyTokens(val clientId: ClientId, val tokens: Tokens = Tokens())
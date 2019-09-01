package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens

class RoulettePlayer(clientId: ClientId, private val tokens: Tokens = Tokens()) {

    private val id: RoulettePlayerId = RoulettePlayerId(clientId.value)

    fun id(): RoulettePlayerId = id

    fun tokens(): Tokens = Tokens(tokens.count)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RoulettePlayer

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}
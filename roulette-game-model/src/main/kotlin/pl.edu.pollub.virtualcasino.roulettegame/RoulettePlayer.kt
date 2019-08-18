package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import java.util.*

internal class RoulettePlayer(clientId: ClientId) {

    val id: RoulettePlayerId = RoulettePlayerId(clientId.value)

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

internal data class RoulettePlayerId(val value: UUID = UUID.randomUUID())
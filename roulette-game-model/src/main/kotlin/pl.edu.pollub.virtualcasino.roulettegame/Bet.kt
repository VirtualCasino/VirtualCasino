package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.clientservices.client.Tokens

internal class Bet(private val field: RouletteField, private val playerId: RoulettePlayerId, private var value: Tokens) {

    internal fun field(): RouletteField = field

    internal fun value(): Tokens = Tokens(value.count)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Bet

        if (field != other.field) return false
        if (playerId != other.playerId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field.hashCode()
        result = 31 * result + playerId.hashCode()
        return result
    }

}
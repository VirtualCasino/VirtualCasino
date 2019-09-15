package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens

class RoulettePlayer(clientId: ClientId, private var tokens: Tokens = Tokens()) {

    private val id: RoulettePlayerId = RoulettePlayerId(clientId.value)

    private val placedBets = mutableMapOf<RouletteField, Bet>()

    fun id(): RoulettePlayerId = id

    fun tokens(): Tokens = Tokens(tokens.count)

    internal fun placedBetsValue(): Tokens = placedBets.values.fold(Tokens()) { placedTokens, betValue -> placedTokens + betValue.value() }

    internal fun freeTokens(): Tokens = tokens() - placedBetsValue()

    internal fun placeBet(field: RouletteField, value: Tokens) {
        placedBets.put(field, Bet(field, id, value))
    }

    internal fun cancelBet(field: RouletteField) {
        placedBets.remove(field)
    }

    internal fun chargeForBets(fieldDrawn: NumberField) {
        var charge = 0
        //side effect, mutability and primitive type for optimization
        placedBets.values.forEach{
            if(it.isWon(fieldDrawn)) charge += it.tokensToWin().count
            else charge -= it.value().count
        }
        tokens += Tokens(charge)
        placedBets.clear()
    }

    internal fun placedBets(): Set<Bet> = placedBets.values.toSet()

    internal fun placedBetsFields(): Set<RouletteField> = placedBets.keys

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
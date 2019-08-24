package pl.edu.pollub.virtualcasino.roulettegame

interface RouletteGameRepository {

    fun add(aggregate: RouletteGame): Boolean

    fun find(id: RouletteGameId): RouletteGame?

    fun clear()

    fun contains(id: RouletteGameId): Boolean

}
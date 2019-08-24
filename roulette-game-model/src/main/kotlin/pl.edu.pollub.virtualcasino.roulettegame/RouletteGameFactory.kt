package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.DomainEvent

interface RouletteGameFactory {

    fun create(aggregateId: RouletteGameId = RouletteGameId(), events: List<DomainEvent> = emptyList()): RouletteGame

}
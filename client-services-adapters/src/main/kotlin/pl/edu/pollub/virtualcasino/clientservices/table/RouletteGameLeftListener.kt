package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation.*
import org.springframework.transaction.annotation.Transactional
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotExist
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableNotExist
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft

@Component
class RouletteGameLeftListener(
        private val clientRepository: ClientRepository,
        private val tableRepository: TableRepository
): DomainEventListener<RouletteGameLeft> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteGameLeft)
    }

    @Transactional(rollbackFor = [DomainException::class])
    fun reactTo(event: RouletteGameLeft) {
        chargeClient(event)
        leaveTable(event)
    }

    @Transactional(rollbackFor = [DomainException::class], propagation = MANDATORY)
    fun chargeClient(event: RouletteGameLeft) {
        val clientId = ClientId(event.playerId)
        val client = clientRepository.find(clientId) ?: throw ClientNotExist(clientId)
        client.`when`(event)
    }

    @Transactional(rollbackFor = [DomainException::class], propagation = NESTED)
    fun leaveTable(event: RouletteGameLeft) {
        val tableId = TableId(event.gameId)
        val table = tableRepository.find(tableId) ?: throw TableNotExist(tableId)
        table.`when`(event)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteGameLeft

}
package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableNotExist
import pl.edu.pollub.virtualcasino.roulettegame.events.RouletteGameLeft

@Component
@Transactional(rollbackFor = [DomainException::class])
class RouletteGameLeftListener(private val repository: TableRepository): DomainEventListener<RouletteGameLeft> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteGameLeft)
    }

    private fun reactTo(event: RouletteGameLeft) {
        val tableId = TableId(event.gameId)
        val table = repository.find(tableId) ?: throw TableNotExist(tableId)
        table.`when`(event)
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteGameLeft

}
package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager.*
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.DomainEventListener
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved

@Component
@Transactional(rollbackFor = [DomainException::class])
class RouletteTableReservedListener(private val factory: RouletteGameFactory,
                                    private val repository: RouletteGameRepository,
                                    private val spinScheduler: SpinScheduler
): DomainEventListener<RouletteTableReserved> {

    override fun reactTo(event: DomainEvent) {
        reactTo(event as RouletteTableReserved)
    }

    private fun reactTo(event: RouletteTableReserved) {
        val rouletteGame = factory.create(RouletteGameId(event.tableId))
        rouletteGame.`when`(event)
        repository.add(rouletteGame)
        registerSynchronization(
                object : TransactionSynchronizationAdapter() {

                    override fun afterCommit() {
                        spinScheduler.scheduleTheStartOfFirstSpinForGame(rouletteGame.id())
                    }

                }
        )
    }

    override fun isListenFor(event: DomainEvent): Boolean = event is RouletteTableReserved

}
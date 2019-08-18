package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.clientservices.table.commands.JoinToTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.clientservices.table.commands.ReserveRouletteTable
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableNotExist

@Component
@Transactional(rollbackFor = [DomainException::class])
class TableCommandHandler(private val factory: TableFactory,
                          private val repository: TableRepository) {

    fun handle(command: ReservePokerTable): TableId {
        val tableToReserve = factory.create()
        tableToReserve.handle(command)
        repository.add(tableToReserve)
        return tableToReserve.id
    }

    fun handle(command: ReserveRouletteTable): TableId {
        val tableToReserve = factory.create()
        tableToReserve.handle(command)
        repository.add(tableToReserve)
        return tableToReserve.id
    }

    fun handle(command: JoinToTable): TableId {
        val tableId = command.tableId
        val tableToJoin = repository.find(tableId) ?: throw TableNotExist(tableId)
        tableToJoin.handle(command)
        return tableToJoin.id
    }

}
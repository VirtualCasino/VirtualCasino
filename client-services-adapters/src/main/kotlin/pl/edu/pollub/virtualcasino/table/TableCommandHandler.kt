package pl.edu.pollub.virtualcasino.table

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.table.commands.JoinToTable
import pl.edu.pollub.virtualcasino.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.table.commands.ReserveRouletteTable
import pl.edu.pollub.virtualcasino.table.exceptions.TableNotExist

@Component
@Transactional(rollbackFor = [DomainException::class])
class TableCommandHandler(private val tableFactory: TableFactory,
                          private val tableRepository: TableRepository) {

    fun handle(command: ReservePokerTable): TableId {
        val tableToReserve = tableFactory.create()
        tableToReserve.handle(command)
        tableRepository.add(tableToReserve)
        return tableToReserve.id
    }

    fun handle(command: ReserveRouletteTable): TableId {
        val tableToReserve = tableFactory.create()
        tableToReserve.handle(command)
        tableRepository.add(tableToReserve)
        return tableToReserve.id
    }

    fun handle(command: JoinToTable): TableId {
        val tableId = command.tableId
        val tableToJoin = tableRepository.find(tableId) ?: throw TableNotExist(tableId)
        tableToJoin.handle(command)
        return tableToJoin.id
    }

}
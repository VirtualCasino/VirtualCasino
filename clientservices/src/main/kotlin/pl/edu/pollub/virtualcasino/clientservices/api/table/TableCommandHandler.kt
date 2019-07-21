package pl.edu.pollub.virtualcasino.clientservices.api.table

import org.springframework.stereotype.Service
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.JoinToTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.TableNotExist

@Service
class TableCommandHandler(private val tableFactory: TableFactory,
                          private val tableRepository: TableRepository) {

    fun handle(command: ReserveTable): TableId {
        val tableToReserve = tableFactory.create()
        tableToReserve.handle(command)
        tableRepository.add(tableToReserve)
        return tableToReserve.id
    }

    fun handle(command: JoinToTable): TableId {
        val tableId = command.tableId
        val tableToJoin = tableRepository.find(tableId) ?: throw TableNotExist(tableId)
        tableToJoin.handle(command)
        tableRepository.add(tableToJoin) //should be replaced with dirty checking...
        return tableToJoin.id
    }

}
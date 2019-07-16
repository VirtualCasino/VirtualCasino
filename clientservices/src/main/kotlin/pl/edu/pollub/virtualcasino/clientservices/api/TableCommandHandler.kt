package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.stereotype.Service
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable

@Service
class TableCommandHandler(val tableFactory: TableFactory,
                          val tableRepository: TableRepository) {

    fun handle(command: ReserveTable) {
        val table = tableFactory.create()
        table.handle(command)
        tableRepository.add(table)
    }

}
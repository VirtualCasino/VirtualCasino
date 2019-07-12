package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.stereotype.Service
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientDoesNotExist
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientIsBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable

@Service
class TableFacade(val tableFactory: TableFactory,
                  val tableRepository: TableRepository,
                  val clientRepository: ClientRepository) {

    fun handle(command: ReserveTable): TableId {
        val clientId = command.clientId
        val client = clientRepository.find(clientId) ?: throw ClientDoesNotExist(clientId)
        if(client.isBusy()) throw ClientIsBusy(clientId)
        val table = tableFactory.create()
        table.handle(command)
        tableRepository.add(table)
        return table.id
    }

}
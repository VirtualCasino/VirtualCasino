package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.stereotype.Service
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientDoesNotExist
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable

@Service
class TableCommandHandler(val tableRepository: TableRepository,
                          val clientRepository: ClientRepository) {

    fun handle(command: ReserveTable) {
        val clientId = command.clientId
        val client = clientRepository.find(clientId) ?: throw ClientDoesNotExist(clientId)
        client.handle(command)
    }

}
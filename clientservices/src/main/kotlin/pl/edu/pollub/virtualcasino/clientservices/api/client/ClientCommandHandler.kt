package pl.edu.pollub.virtualcasino.clientservices.api.client

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientNotExist

@Component
class ClientCommandHandler(private val clientRepository: ClientRepository) {

    fun handle(command: BuyTokens) {
        val client = clientRepository.find(command.clientId) ?: throw ClientNotExist(command.clientId)
        client.handle(command)
        clientRepository.add(client)//should be replaced with dirty checking...
    }

}
package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.commands.RegisterClient
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotExist

@Component
@Transactional(rollbackFor = [DomainException::class])
class ClientCommandHandler(private val factory: ClientFactory, private val repository: ClientRepository) {

    fun handle(command: RegisterClient): ClientId {
        val clientToRegister = factory.create()
        clientToRegister.handle(command)
        repository.add(clientToRegister)
        return clientToRegister.id()
    }

    fun handle(command: BuyTokens) {
        val client = repository.find(command.clientId) ?: throw ClientNotExist(command.clientId)
        client.handle(command)
    }

}
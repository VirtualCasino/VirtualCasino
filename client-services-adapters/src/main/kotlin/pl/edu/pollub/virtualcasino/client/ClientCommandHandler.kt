package pl.edu.pollub.virtualcasino.client

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.client.exceptions.ClientNotExist

@Component
@Transactional(rollbackFor = [DomainException::class])
class ClientCommandHandler(private val clientRepository: ClientRepository) {

    fun handle(command: BuyTokens) {
        val client = clientRepository.find(command.clientId) ?: throw ClientNotExist(command.clientId)
        client.handle(command)
    }

}
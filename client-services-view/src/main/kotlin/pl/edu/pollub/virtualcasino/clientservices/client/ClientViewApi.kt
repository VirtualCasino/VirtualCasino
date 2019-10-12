package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotExist
import java.util.*

@RestController
@RequestMapping("/virtual-casino/casino-services/clients")
class ClientViewApi(private val repository: ClientViewRepository) {

    @GetMapping("/{clientId}")
    fun getClientById(@PathVariable clientId: UUID): ClientView = repository.find(clientId) ?: throw ClientNotExist(ClientId(clientId))

}


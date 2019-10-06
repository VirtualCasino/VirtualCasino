package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import java.util.*

@RestController
@RequestMapping("/virtual-casino/casino-services/clients")
class ClientApi(private val commandHandler: ClientCommandHandler, private val repository: ClientRepository, private val factory: ClientFactory) {


    @PostMapping
    fun createClient(@RequestBody request: CreateClientRequest): ResponseEntity<Any> {
        repository.add(factory.create(ClientId(request.clientId)))
        return ResponseEntity.noContent().build()
    }

    @PutMapping("/tokens")
    fun handle(@RequestBody command: BuyTokens): ResponseEntity<Any> {
        commandHandler.handle(command)
        return ResponseEntity.noContent().build()
    }

}

class CreateClientRequest(val clientId: UUID)

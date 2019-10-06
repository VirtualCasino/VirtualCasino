package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.commands.RegisterClient
import java.net.URI

@RestController
@RequestMapping("/virtual-casino/casino-services/clients")
class ClientApi(private val commandHandler: ClientCommandHandler) {


    @PostMapping
    fun handle(@RequestBody command: RegisterClient): ResponseEntity<Any> {
        val registeredClientId = commandHandler.handle(command)
        return ResponseEntity.created(URI.create("/virtual-casino/casino-services/clients/${registeredClientId.value}")).build()
    }

    @PutMapping("/tokens")
    fun handle(@RequestBody command: BuyTokens): ResponseEntity<Any> {
        commandHandler.handle(command)
        return ResponseEntity.noContent().build()
    }

}

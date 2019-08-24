package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens

@RestController
@RequestMapping("/virtual-casino/casino-services/clients")
class ClientApi(private val commandHandler: ClientCommandHandler) {

    @PutMapping("/tokens")
    fun handle(@RequestBody command: BuyTokens): ResponseEntity<Any> {
        commandHandler.handle(command)
        return ResponseEntity.noContent().build()
    }

}
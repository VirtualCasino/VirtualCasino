package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable
import java.net.URI

@RestController
@RequestMapping("/tables")
class TableApi(private val commandHandler: TableCommandHandler) {

    @PostMapping
    fun reserveTable(@RequestBody command: ReserveTable): ResponseEntity<Any> {
        commandHandler.handle(command)
        return ResponseEntity.created(URI.create("/clients/${command.clientId}/tables")).build()
    }

}
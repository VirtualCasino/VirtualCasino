package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.JoinToTable
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.ReserveTable
import java.net.URI

@RestController
@RequestMapping("/tables")
class TableApi(private val commandHandler: TableCommandHandler) {

    @PostMapping
    fun handle(@RequestBody command: ReserveTable): ResponseEntity<URI> {
        val reservedTableId = commandHandler.handle(command)
        return ResponseEntity.created(URI.create("/tables/${reservedTableId.value}")).build()
    }

    @PostMapping("/participation")
    fun handle(@RequestBody command: JoinToTable): ResponseEntity<URI> {
        val joinedTableId = commandHandler.handle(command)
        return ResponseEntity.created(URI.create("/tables/${joinedTableId.value}")).build()
    }

}
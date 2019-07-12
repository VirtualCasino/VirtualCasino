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
class TableApi(private val facade: TableFacade) {

    @PostMapping
    fun reserveTable(@RequestBody command: ReserveTable): ResponseEntity<Any> {
        val createdTableId = facade.handle(command)
        return ResponseEntity.created(URI.create("/tables/${createdTableId.value}")).build()
    }

}
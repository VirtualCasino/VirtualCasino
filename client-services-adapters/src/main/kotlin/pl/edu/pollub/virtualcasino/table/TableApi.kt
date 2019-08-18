package pl.edu.pollub.virtualcasino.table

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pollub.virtualcasino.table.commands.JoinToTable
import pl.edu.pollub.virtualcasino.table.commands.ReservePokerTable
import pl.edu.pollub.virtualcasino.table.commands.ReserveRouletteTable
import java.net.URI

@RestController
@RequestMapping("/casino-services/tables")
class TableApi(private val commandHandler: TableCommandHandler) {

    @PostMapping("/poker")
    fun handle(@RequestBody command: ReservePokerTable): ResponseEntity<URI> {
        val reservedTableId = commandHandler.handle(command)
        return ResponseEntity.created(URI.create("/tables/${reservedTableId.value}")).build()
    }

    @PostMapping("/roulette")
    fun handle(@RequestBody command: ReserveRouletteTable): ResponseEntity<URI> {
        val reservedTableId = commandHandler.handle(command)
        return ResponseEntity.created(URI.create("/tables/${reservedTableId.value}")).build()
    }

    @PostMapping("/participation")
    fun handle(@RequestBody command: JoinToTable): ResponseEntity<URI> {
        val joinedTableId = commandHandler.handle(command)
        return ResponseEntity.created(URI.create("/tables/${joinedTableId.value}")).build()
    }

}
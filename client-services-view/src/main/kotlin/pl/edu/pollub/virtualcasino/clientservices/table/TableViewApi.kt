package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.web.bind.annotation.*
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableNotExist
import java.util.*

@RestController
@RequestMapping("/virtual-casino/casino-services/tables")
class TableViewApi(private val repository: TableViewRepository) {

    @GetMapping("/{tableId}")
    fun getTableById(@PathVariable tableId: UUID): TableView = repository.find(tableId) ?: throw TableNotExist(TableId(tableId))

    @GetMapping("/roulette")
    fun getAllTableViews(@RequestParam searchedPlayerNick: String?): List<TableView> {
        return repository.findAllOpenRouletteTables(searchedPlayerNick ?: "")
    }

}
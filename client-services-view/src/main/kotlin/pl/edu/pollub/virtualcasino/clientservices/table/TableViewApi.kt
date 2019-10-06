package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/virtual-casino/casino-services/tables")
class TableViewApi(private val repository: TableViewRepository) {

    @GetMapping("/roulette")
    fun getAllTableViews(): List<TableView> {
        return repository.findAll()
    }

}
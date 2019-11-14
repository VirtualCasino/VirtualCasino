package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableNotExist
import java.util.*

@RestController
@RequestMapping("/virtual-casino/casino-services/roulette-games")
class RouletteGameViewApi(private val repository: RouletteGameViewRepository) {

    @GetMapping("/{gameId}")
    fun getRouletteGameById(@PathVariable gameId: UUID): RouletteGameView = repository.find(gameId) ?: throw TableNotExist(TableId(gameId))

}

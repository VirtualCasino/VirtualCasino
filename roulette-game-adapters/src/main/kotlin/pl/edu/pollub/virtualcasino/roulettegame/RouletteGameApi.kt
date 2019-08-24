package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import pl.edu.pollub.virtualcasino.roulettegame.commands.LeaveRouletteGame

@RestController
@RequestMapping("/virtual-casino/roulette-game/games")
class RouletteGameApi(private val commandHandler: RouletteGameCommandHandler) {

    @DeleteMapping("/players")
    @ResponseStatus(value = NO_CONTENT)
    fun handle(@RequestBody command: LeaveRouletteGame) {
        commandHandler.handle(command)
    }

}
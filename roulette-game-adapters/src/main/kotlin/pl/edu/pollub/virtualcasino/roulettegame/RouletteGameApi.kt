package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.http.HttpStatus.*
import org.springframework.web.bind.annotation.*
import pl.edu.pollub.virtualcasino.roulettegame.commands.CancelRouletteBet
import pl.edu.pollub.virtualcasino.roulettegame.commands.LeaveRouletteGame
import pl.edu.pollub.virtualcasino.roulettegame.commands.PlaceRouletteBet

@RestController
@RequestMapping("/virtual-casino/roulette-game/games")
class RouletteGameApi(private val commandHandler: RouletteGameCommandHandler) {

    @PostMapping("/bets")
    @ResponseStatus(value = OK) //this status should be replaced with URI to bet from view module
    fun handle(@RequestBody command: PlaceRouletteBet) {
        commandHandler.handle(command)
    }

    @DeleteMapping("/bets")
    @ResponseStatus(value = NO_CONTENT)
    fun handle(@RequestBody command: CancelRouletteBet) {
        commandHandler.handle(command)
    }

    @DeleteMapping("/players")
    @ResponseStatus(value = NO_CONTENT)
    fun handle(@RequestBody command: LeaveRouletteGame) {
        commandHandler.handle(command)
    }

}
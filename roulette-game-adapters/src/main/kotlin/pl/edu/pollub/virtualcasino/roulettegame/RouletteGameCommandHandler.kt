package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import pl.edu.pollub.virtualcasino.DomainException
import pl.edu.pollub.virtualcasino.roulettegame.commands.*
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.RouletteGameNotExist

@Component
@Transactional(rollbackFor = [DomainException::class])
class RouletteGameCommandHandler(private val repository: RouletteGameRepository) {

    fun handle(command: StartSpin) {
        val game = repository.find(command.gameId) ?: throw RouletteGameNotExist(command.gameId)
        game.handle(command)
    }

    fun handle(command: PlaceRouletteBet) {
        val game = repository.find(command.gameId) ?: throw RouletteGameNotExist(command.gameId)
        game.handle(command)
    }

    fun handle(command: CancelRouletteBet) {
        val game = repository.find(command.gameId) ?: throw RouletteGameNotExist(command.gameId)
        game.handle(command)
    }

    fun handle(command: FinishSpin) {
        val game = repository.find(command.gameId) ?: throw RouletteGameNotExist(command.gameId)
        game.handle(command)
    }

    fun handle(command: LeaveRouletteGame) {
        val game = repository.find(command.gameId) ?: throw RouletteGameNotExist(command.gameId)
        game.handle(command)
    }

}
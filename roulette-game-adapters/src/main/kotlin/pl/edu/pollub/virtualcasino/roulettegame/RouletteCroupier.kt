package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionSynchronizationAdapter
import org.springframework.transaction.support.TransactionSynchronizationManager.*
import pl.edu.pollub.virtualcasino.roulettegame.commands.FinishSpin
import pl.edu.pollub.virtualcasino.roulettegame.commands.StartSpin
import java.time.Clock
import java.time.Clock.systemDefaultZone

@Component
class RouletteCroupier(private val rouletteWheel: RouletteWheel,
                       private val spinConfig: SpinConfig,
                       private val clock: Clock,
                       private val taskScheduler: TaskScheduler,
                       private val commandHandler: RouletteGameCommandHandler) {

    fun planTheStartOfFirstSpinForGame(gameId: RouletteGameId) {
        planStartSpin(gameId, spinConfig.startSpinDelayAfterTableReservationInMilliseconds)
    }

    private fun planTheStartOfEveryNextSpinForGame(gameId: RouletteGameId) {
        planStartSpin(gameId, spinConfig.breakTimeBetweenSpinsInMilliseconds)
    }

    private fun planStartSpin(gameId: RouletteGameId, delay: Long) {
        val currentPointInTime = clock.instant()
        val endBettingPointInTime = currentPointInTime.plusMillis(spinConfig.bettingTimeInMilliseconds)
        val command = StartSpin(gameId, endBettingPointInTime)
        val startSpinTimeAfterTableReservationPointInTime = currentPointInTime.plusMillis(delay)
        taskScheduler.schedule({
            commandHandler.handle(command)
            planTheFinishOfSpinForGame(gameId)
        }, startSpinTimeAfterTableReservationPointInTime)
    }

    private fun planTheFinishOfSpinForGame(gameId: RouletteGameId) {
        val currentPointInTime = clock.instant()
        val finishSpinPointInTime = currentPointInTime.plusMillis(spinConfig.spinTimeDurationInMilliseconds)
        val drawnField = rouletteWheel.drawnField()
        val command = FinishSpin(gameId, drawnField)
        taskScheduler.schedule({
            commandHandler.handle(command)
            planTheStartOfEveryNextSpinForGame(gameId)
        }, finishSpinPointInTime)
    }
}

@Configuration
@ConfigurationProperties(prefix = "spin")
class SpinConfig {

    var startSpinDelayAfterTableReservationInMilliseconds: Long = 10000 // 10 seconds

    var bettingTimeInMilliseconds: Long = 240000 // 4 minutes

    var spinTimeDurationInMilliseconds: Long = 300000 // 5 minutes

    var breakTimeBetweenSpinsInMilliseconds: Long = 20000 // 20 seconds

}

@Configuration
@Profile("roulette-game")
class RouletteTimeConfig {

    @Bean
    fun clock(): Clock = systemDefaultZone()

}
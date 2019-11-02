package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.scheduling.TaskScheduler
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.roulettegame.commands.FinishSpin
import pl.edu.pollub.virtualcasino.roulettegame.commands.StartSpin
import java.time.Clock
import java.time.Clock.systemDefaultZone
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler



@Component
class SpinScheduler(private val rouletteWheel: RouletteWheel,
                    private val spinConfig: SpinConfig,
                    private val clock: Clock,
                    private val taskScheduler: TaskScheduler,
                    private val commandHandler: RouletteGameCommandHandler): RouletteCroupier {

    override fun scheduleTheStartOfFirstSpinForGame(gameId: RouletteGameId) {
        scheduleTheStartOfSpin(gameId, spinConfig.startSpinDelayAfterTableReservationInMilliseconds)
    }

    override fun scheduleTheStartOfSpinForGame(gameId: RouletteGameId) {
        scheduleTheStartOfSpin(gameId, spinConfig.breakTimeBetweenSpinsInMilliseconds)
    }

    private fun scheduleTheStartOfSpin(gameId: RouletteGameId, delay: Long) {
        val currentPointInTime = clock.instant()
        val endBettingPointInTime = currentPointInTime.plusMillis(spinConfig.bettingTimeInMilliseconds)
        val command = StartSpin(gameId, endBettingPointInTime)
        val startSpinTimeAfterTableReservationPointInTime = currentPointInTime.plusMillis(delay)
        taskScheduler.schedule({ commandHandler.handle(command) }, startSpinTimeAfterTableReservationPointInTime)
    }

    override fun scheduleTheFinishOfSpinForGame(gameId: RouletteGameId) {
        val currentPointInTime = clock.instant()
        val finishSpinPointInTime = currentPointInTime.plusMillis(spinConfig.spinTimeDurationInMilliseconds)
        taskScheduler.schedule({
            val drawnField = rouletteWheel.drawnField()
            val command = FinishSpin(gameId, drawnField)
            commandHandler.handle(command)
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
    @Primary
    fun clock(): Clock = systemDefaultZone()

}

@Configuration
class TaskSchedulerConfiguration {

    @Bean
    fun taskScheduler(): TaskScheduler {
        val scheduler = ThreadPoolTaskScheduler()
        scheduler.threadNamePrefix = "poolScheduler"
        scheduler.poolSize = 10
        return scheduler
    }

}
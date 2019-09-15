package pl.edu.pollub.virtualcasino.roulettegame

import java.time.Clock
import java.time.Instant

internal interface RouletteGameState {

    fun isSpinStarted(): Boolean

    fun isBettingTimeEnded(): Boolean

    fun isSpinFinished(): Boolean

}

internal class SpinNotStartYet: RouletteGameState {

    override fun isSpinStarted(): Boolean = false

    override fun isBettingTimeEnded(): Boolean = false

    override fun isSpinFinished(): Boolean = true

}

internal class SpinStarted(private val clock: Clock, private val bettingTimeEnd: Instant): RouletteGameState {

    override fun isSpinStarted(): Boolean = true

    override fun isBettingTimeEnded(): Boolean = bettingTimeEnd < clock.instant()

    override fun isSpinFinished(): Boolean = false

}

internal class SpinFinished: RouletteGameState {

    override fun isSpinStarted(): Boolean = true

    override fun isBettingTimeEnded(): Boolean = true

    override fun isSpinFinished(): Boolean = true

}
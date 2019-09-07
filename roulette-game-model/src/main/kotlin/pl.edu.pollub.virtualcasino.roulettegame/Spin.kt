package pl.edu.pollub.virtualcasino.roulettegame

import java.time.Instant

internal data class Spin(private val bettingTimeEnd: Instant) {

    fun bettingTimeEnd(): Instant = bettingTimeEnd

}
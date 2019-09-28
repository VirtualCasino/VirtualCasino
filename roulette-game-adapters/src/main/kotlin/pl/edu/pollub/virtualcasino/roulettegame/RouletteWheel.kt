package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.util.*

@Component
class RouletteWheel(private val random: Random) {

    fun drawnField(): NumberField {
        val drawnFieldIndex = random.nextInt(NumberField::class.java.enumConstants.size)
        return NumberField::class.java.enumConstants[drawnFieldIndex]
    }

}

@Configuration
@Profile("roulette-game")
class RouletteRandomConfig {

    @Bean
    fun random(): Random = Random()

}
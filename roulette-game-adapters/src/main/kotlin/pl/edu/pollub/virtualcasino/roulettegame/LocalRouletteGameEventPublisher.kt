package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.eventpublisher.LocalEventPublisher

@Component
class LocalRouletteGameEventPublisher: LocalEventPublisher(), RouletteGameEventPublisher
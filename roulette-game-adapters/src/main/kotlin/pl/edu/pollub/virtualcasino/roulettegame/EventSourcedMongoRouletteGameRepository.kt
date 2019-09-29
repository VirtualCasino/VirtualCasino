package pl.edu.pollub.virtualcasino.roulettegame

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.eventstore.EventSourcedMongoRepository

@Component
class EventSourcedMongoRouletteGameRepository(val factory: RouletteGameFactory,
                                              rouletteGameBoundedContextMongoTemplate: MongoTemplate,
                                              objectMapper: ObjectMapper
): EventSourcedMongoRepository<RouletteGame, RouletteGameId>(rouletteGameBoundedContextMongoTemplate, objectMapper), RouletteGameRepository {

    override fun createAggregate(id: RouletteGameId, events: List<DomainEvent>): RouletteGame = factory.create(id, events)

}
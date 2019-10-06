package pl.edu.pollub.virtualcasino.roulettegame

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.eventstore.EventSourcedMongoRepository

@Component
class EventSourcedMongoRouletteGameRepository(val factory: RouletteGameFactory,
                                              rouletteGameBoundedContextMongoTemplate: MongoTemplate,
                                              @Qualifier("rouletteObjectMapper") rouletteObjectMapper: ObjectMapper
): EventSourcedMongoRepository<RouletteGame, RouletteGameId>(rouletteGameBoundedContextMongoTemplate, rouletteObjectMapper), RouletteGameRepository {

    override fun createAggregate(id: RouletteGameId, events: List<DomainEvent>): RouletteGame = factory.create(id, events)

}

package pl.edu.pollub.virtualcasino.roulettegame.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.core.MongoTemplate
import pl.edu.pollub.virtualcasino.eventstore.EventSerializer
import pl.edu.pollub.virtualcasino.eventstore.EventStore
import pl.edu.pollub.virtualcasino.eventstore.ObjectMapperFactory
import pl.edu.pollub.virtualcasino.roulettegame.*

@Configuration
class RouletteGameBoundedContextEventStoreConfig {

    @Bean
    fun rouletteGameBoundedContextEventStore(rouletteGameBoundedContextMongoTemplate: MongoTemplate): EventStore {
        return EventStore(rouletteGameBoundedContextMongoTemplate)
    }

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        val objectMapper = ObjectMapperFactory().create()
        objectMapper.addMixIn(RouletteField::class.java, RouletteFieldInheritanceHierarchy::class.java)
        return objectMapper
    }
    @Bean
    fun rouletteGameBoundedContextEventSerializer(objectMapper: ObjectMapper): EventSerializer = EventSerializer(objectMapper)

}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(name = NumberField.TYPE, value = NumberField::class),
        JsonSubTypes.Type(name = PairField.TYPE, value = PairField::class),
        JsonSubTypes.Type(name = TripleFiled.TYPE, value = TripleFiled::class),
        JsonSubTypes.Type(name = QuarterFiled.TYPE, value = QuarterFiled::class),
        JsonSubTypes.Type(name = HalfDozenFiled.TYPE, value = HalfDozenFiled::class),
        JsonSubTypes.Type(name = DozenFiled.TYPE, value = DozenFiled::class),
        JsonSubTypes.Type(name = ColumnField.TYPE, value = ColumnField::class),
        JsonSubTypes.Type(name = HalfBoardField.TYPE, value = HalfBoardField::class),
        JsonSubTypes.Type(name = ColorField.TYPE, value = ColorField::class),
        JsonSubTypes.Type(name = EvenField.TYPE, value = EvenField::class),
        JsonSubTypes.Type(name = OddField.TYPE, value = OddField::class)
)
interface RouletteFieldInheritanceHierarchy
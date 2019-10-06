package pl.edu.pollub.virtualcasino.roulettegame.config

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.edu.pollub.virtualcasino.eventstore.ObjectMapperFactory
import pl.edu.pollub.virtualcasino.roulettegame.*

@Configuration
class RouletteGameBoundedContextObjectMapperConfig {

    @Bean
    fun rouletteObjectMapper(): ObjectMapper {
        val objectMapper = ObjectMapperFactory().create()
        objectMapper.addMixIn(RouletteField::class.java, RouletteFieldInheritanceHierarchy::class.java)
        return objectMapper
    }

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

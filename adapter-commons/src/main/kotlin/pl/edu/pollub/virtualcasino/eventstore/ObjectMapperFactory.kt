package pl.edu.pollub.virtualcasino.eventstore

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import pl.edu.pollub.virtualcasino.DomainEvent
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import pl.edu.pollub.virtualcasino.clientservices.client.events.ClientRegistered
import pl.edu.pollub.virtualcasino.clientservices.client.events.TokensBought
import pl.edu.pollub.virtualcasino.clientservices.table.events.JoinedTable
import pl.edu.pollub.virtualcasino.clientservices.table.events.PokerTableReserved
import pl.edu.pollub.virtualcasino.clientservices.table.events.RouletteTableReserved
import pl.edu.pollub.virtualcasino.roulettegame.events.*

class ObjectMapperFactory {

    fun create(): ObjectMapper {
        return ObjectMapper()
                .registerModule(KotlinModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .registerModule(JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .addMixIn(DomainEvent::class.java, DomainEventInheritanceHierarchy::class.java)
    }

}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(name = PokerTableReserved.TYPE, value = PokerTableReserved::class),
        JsonSubTypes.Type(name = RouletteTableReserved.TYPE, value = RouletteTableReserved::class),
        JsonSubTypes.Type(name = JoinedTable.TYPE, value = JoinedTable::class),
        JsonSubTypes.Type(name = ClientRegistered.TYPE, value = ClientRegistered::class),
        JsonSubTypes.Type(name = TokensBought.TYPE, value = TokensBought::class),
        JsonSubTypes.Type(name = RouletteGameLeft.TYPE, value = RouletteGameLeft::class),
        JsonSubTypes.Type(name = RouletteBetPlaced.TYPE, value = RouletteBetPlaced::class),
        JsonSubTypes.Type(name = RouletteBetCanceled.TYPE, value = RouletteBetCanceled::class),
        JsonSubTypes.Type(name = SpinStarted.TYPE, value = SpinStarted::class),
        JsonSubTypes.Type(name = SpinFinished.TYPE, value = SpinFinished::class)
)
interface DomainEventInheritanceHierarchy
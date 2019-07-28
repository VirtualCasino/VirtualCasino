package pl.edu.pollub.virtualcasino.clientservices.infrastructure.publisher

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.cloud.stream.messaging.Source.*
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Direction.*
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.integration.annotation.Publisher
import org.springframework.messaging.handler.annotation.Header
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.eventstore.EventDescriptor
import java.util.*

@Component
class EventPublisher(val publishingChannel: PublishingChannel,
                     @Qualifier("casinoServicesWriteTemplate") val mongo: MongoTemplate,
                     val properties: EventsPublishingProperties
) {

    fun publish() {
        findUnpublishedEvents().forEach { sendSafely(it) }
    }

    fun sendSafely(event: EventDescriptor) {
        runCatching {
            val eventBody = event.body
            val eventId = event.id
            publishingChannel.send(eventBody, eventId)
            mongo.save(event.sent())
        }
        .onFailure {
            //TODO add logger with event and information about fail
            System.out.println(it)
        }
    }

    private fun findUnpublishedEvents(): List<EventDescriptor> {
        val query = Query()
        query.addCriteria(Criteria.where("sent").`is`(false))
        query.with(Sort(DESC, "occurredAt"))
        query.limit(properties.limit)
        return mongo.find(query, EventDescriptor::class.java)
    }

}

@Component
class PublishingChannel {

    @Publisher(channel = OUTPUT)
    fun send(payload: String, @Header uuid: UUID): String = payload

}

@ConfigurationProperties(prefix = "events-publishing")
class EventsPublishingProperties {

    var frequency: Int = 1000
    var limit: Int = 100

}
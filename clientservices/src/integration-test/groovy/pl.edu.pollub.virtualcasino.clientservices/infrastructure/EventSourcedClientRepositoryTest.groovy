package pl.edu.pollub.virtualcasino.clientservices.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.edu.pollub.virtualcasino.clientservices.CasinoServicesBoundedContext
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.client.EventSourcedClientRepository
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId

@SpringBootTest(classes = [CasinoServicesBoundedContext.class])
class EventSourcedClientRepositoryTest extends Specification {

    @Subject
    @Autowired
    EventSourcedClientRepository repository

    def cleanup() {
        repository.clear()
    }

    def 'should find stored client'() {
        given:
            def clientId = sampleClientId()
            def client = sampleClient(id: clientId, changes: [])
            repository.add(client)
        when:
            def foundClient = repository.find(clientId)
        then:
            foundClient == client
            foundClient.changes == client.changes
    }

}

package pl.edu.pollub.virtualcasino.clientservices.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import pl.edu.pollub.virtualcasino.clientservices.CasinoServicesBoundedContext
import pl.edu.pollub.virtualcasino.clientservices.infrastructure.table.EventSourcedTableRepository
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTableReserved.sampleTableReserved

@SpringBootTest(classes = [CasinoServicesBoundedContext.class])
class EventSourcedTableRepositoryTest extends Specification {

    @Subject
    @Autowired
    EventSourcedTableRepository repository

    def cleanup() {
        repository.clear()
    }

    def 'should find stored table'() {
        given:
            def tableId = sampleTableId()
            def events = [sampleTableReserved(tableId: tableId)]
            def table = sampleTable(id: tableId, changes: events)
            repository.add(table)
        when:
            def foundTable = repository.find(tableId)
        then:
            foundTable == table
    }

    def 'should contains table wich participation of client which reserved table'() {
        given:
            def tableId = sampleTableId()
            def clientId = sampleClientId()
            def events = [sampleTableReserved(tableId: tableId, clientId: clientId)]
            def table = sampleTable(id: tableId, changes: events)
            repository.add(table)
        and:
            def clientParticipation = sampleParticipation(clientId: clientId)
        expect:
            repository.containsWithParticipation(clientParticipation)
    }

}

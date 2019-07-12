package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import pl.edu.pollub.virtualcasino.clientservices.CasinoServicesBoundedContext
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleReserveTable.sampleReserveTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleParticipation

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [CasinoServicesBoundedContext.class])
class TableApiTest extends Specification {

    @Autowired
    TableRepository tableRepository

    @Autowired
    ClientRepository clientRepository

    @Autowired
    TestRestTemplate http

    def cleanup() {
        clientRepository.clear()
        tableRepository.clear()
    }

    def 'should reserve table'() {
        given:
            def clientId = sampleClientId()
            def client = sampleClient(id: clientId)
            clientRepository.add(client)
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        and:
            def reserveTable = sampleReserveTable(clientId: clientId)
        when:
            def tableUri = http.postForLocation(URI.create("/tables"), reserveTable)
        then:
            def tableId = toTableId(tableUri)
            def table = tableRepository.find(tableId)
            table != null
            table.hasParticipation(expectedParticipation)
    }

    //TODO dopisać testy przypadków negatywnych (Exception handler)

    def toTableId(URI tableUri) {
        def tableUriString = tableUri.toString()
        return new TableId(tableUriString.substring(tableUriString.lastIndexOf("/") + 1))
    }
}

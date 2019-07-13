package pl.edu.pollub.virtualcasino.clientservices.api

import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientDoesNotExist
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientIsBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.client.fakes.FakedClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes.FakedTableRepository
import spock.lang.Specification

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleReserveTable.sampleReserveTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTableReserved.sampleTableReserved
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId

class TableCommandHandlerTest extends Specification {

    def tableRepository = new FakedTableRepository()
    def clientRepository = new FakedClientRepository()

    def sut = new TableCommandHandler(tableRepository, clientRepository)

    def cleanup() {
        tableRepository.clear()
        clientRepository.clear()
    }

    def "should not throw any exceptions when client which reserve table exists"() {
        given:
            def clientId = sampleClientId()
            def client = sampleClient(id: clientId)
            clientRepository.add(client)
        and:
            def reserveTable = sampleReserveTable(clientId: clientId)
        when:
            sut.handle(reserveTable)
        then:
            noExceptionThrown()
    }

    def "should throw ClientNotFound when client which reserve table doesn't exist"() {
        given:
            def clientId = sampleClientId()
            def reserveTable = sampleReserveTable(clientId: clientId)
        when:
            sut.handle(reserveTable)
        then:
            def e = thrown(ClientDoesNotExist)
            e.clientId == clientId
    }

}

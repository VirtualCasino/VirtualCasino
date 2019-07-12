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

class TableFacadeTest extends Specification {

    def tableFactory = Stub(TableFactory)
    def tableRepository = new FakedTableRepository()
    def clientRepository = new FakedClientRepository()

    def sut = new TableFacade(tableFactory, tableRepository, clientRepository)

    def setup() {
        tableFactory.create(*_) >> sampleTable()
    }

    def cleanup() {
        tableRepository.clear()
        clientRepository.clear()
    }

    def "should add new table to repository when client didn't reserve any other table"() {
        given:
            def client = sampleClient(tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def reserveTable = sampleReserveTable(clientId: client.id)
        when:
            sut.handle(reserveTable)
        then:
            def tables = tableRepository.findAll()
            tables.size() == 1
    }

    def "should throw ClientIsBusy when client already reserved other table"() {
        given:
            def client = sampleClient(tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def tableId = sampleTableId()
            def tableReserved = sampleTableReserved(tableId: tableId, clientId: client.id)
            def table = sampleTable(changes: [tableReserved])
            tableRepository.add(table)
        and:
            def reserveTable = sampleReserveTable(clientId: client.id)
        when:
            sut.handle(reserveTable)
        then:
            def e = thrown(ClientIsBusy)
            e.clientId == client.id
    }

    def "should throw ClientNotFound when client which reserving table doesn't exist"() {
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

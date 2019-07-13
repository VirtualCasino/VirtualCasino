package pl.edu.pollub.virtualcasino.clientservices.domain.client

import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientIsBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes.FakedTableRepository
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleReserveTable.sampleReserveTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTableReserved.sampleTableReserved
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId

class ClientTest extends Specification {

    def tableFactory = new TableFactory()
    def tableRepository = new FakedTableRepository()

    @Subject
    def client

    def setup() {
        tableRepository.clear()
    }


    def "should add new table to repository when client didn't reserve any other table"() {
        given:
            client = sampleClient(tableFactory: tableFactory, tableRepository: tableRepository)
        and:
            def reserveTable = sampleReserveTable(clientId: client.id)
        when:
            client.handle(reserveTable)
        then:
            def tables = tableRepository.findAll()
            tables.size() == 1
    }

    def "should throw ClientIsBusy when client already reserved other table"() {
        given:
            client = sampleClient(tableRepository: tableRepository)
        and:
            def tableId = sampleTableId()
            def reservedTable = sampleTableReserved(tableId: tableId, clientId: client.id)
            def table = sampleTable(changes: [reservedTable])
            tableRepository.add(table)
        and:
            def reserveTable = sampleReserveTable(clientId: client.id)
        when:
            client.handle(reserveTable)
        then:
            def e = thrown(ClientIsBusy)
            e.clientId == client.id
    }

}

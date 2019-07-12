package pl.edu.pollub.virtualcasino.clientservices.domain.client

import pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes.FakedTableRepository
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTableReserved.sampleTableReserved
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId

class ClientTest extends Specification {

    def tableRepository = new FakedTableRepository()

    @Subject
    def client

    def setup() {
        tableRepository.clear()
    }

    def "should not be busy when table with it's participation doesn't exist"() {
        given:
            client = sampleClient(tableRepository: tableRepository)
        expect:
            !client.isBusy()
    }

    def "should be busy when table with it's participation exists"() {
        given:
            client = sampleClient(tableRepository: tableRepository)
        and:
            def tableId = sampleTableId()
            def tableReserved = sampleTableReserved(tableId: tableId, clientId: client.id)
            def table = sampleTable(changes: [tableReserved])
            tableRepository.add(table)
        expect:
            client.isBusy()
    }


}

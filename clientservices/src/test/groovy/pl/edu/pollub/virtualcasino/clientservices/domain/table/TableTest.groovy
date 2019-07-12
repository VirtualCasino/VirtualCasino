package pl.edu.pollub.virtualcasino.clientservices.domain.table

import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleReserveTable.sampleReserveTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTable

class TableTest extends Specification {

    @Subject
    def table

    def "should has participation of client that open to table"() {
        given:
            table = sampleTable()
            def clientId = sampleClientId()
            def reserveTable = sampleReserveTable(clientId: clientId)
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            table.hasParticipation(expectedParticipation)
    }

}

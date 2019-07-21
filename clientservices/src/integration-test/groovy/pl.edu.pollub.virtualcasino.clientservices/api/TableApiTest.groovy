package pl.edu.pollub.virtualcasino.clientservices.api

import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.InitialBidingRateTooHigh
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.TableFull
import static org.springframework.http.HttpStatus.*
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands.SampleReserveTable.sampleReserveTable

class TableApiTest extends ClientServicesApiTest {

    def "should reserve table"() {
        given:
            def clientId = prepareClient()
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        when:
            reserveTable(clientId)
        then:
            tableRepository.containsWithParticipation(expectedParticipation)
    }

    def "should join client to reserved table"() {
        given:
            def clientThatReservedTableId = prepareClient()
        and:
            def reservedTableId = reserveTable(clientThatReservedTableId)
        and:
            def clientThatWantJoinTableId = prepareClient()
        and:
            def expectedParticipation = sampleParticipation(clientId: clientThatWantJoinTableId)
        when:
            joinTable(reservedTableId, clientThatWantJoinTableId)
        then:
            tableRepository.containsWithParticipation(expectedParticipation)
    }

    def "should not reserve table multiple times by the same client"() {
        given:
            def clientThatReservedTableId = prepareClient()
        and:
            reserveTable(clientThatReservedTableId)
        and:
            def reserveTable = sampleReserveTable(clientId: clientThatReservedTableId)
        when:
            def response = http.postForEntity(URI.create("/tables"), reserveTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.params == ["clientId": clientThatReservedTableId.value]
    }

    def "should not join client to table when client already reserved other table"() {
        given:
            def clientThatReservedTableId = prepareClient()
        and:
            reserveTable(clientThatReservedTableId)
        and:
            def otherClientThatReservedOtherTableId = prepareClient()
        and:
            def otherReservedTableId = reserveTable(otherClientThatReservedOtherTableId)
        and:
            def joinOtherTable = sampleJoinTable(clientId: clientThatReservedTableId, tableId: otherReservedTableId)
        when:
            def response = http.postForEntity(URI.create("/tables/participation"), joinOtherTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.params == ["clientId": clientThatReservedTableId.value]
    }

    def "should not join client to reserved table when client already joined to other table"() {
        given:
            def clientThatReservedTableId = prepareClient()
        and:
            def tableId = reserveTable(clientThatReservedTableId)
        and:
            def clientThatReservedOtherTableId = prepareClient()
        and:
            def otherTableId = reserveTable(clientThatReservedOtherTableId)
        and:
            def clientThatJoinedTableId = prepareClient()
        and:
            joinTable(tableId, clientThatJoinedTableId)
        and:
            def joinToOtherTable = sampleJoinTable(tableId: otherTableId, clientId: clientThatJoinedTableId)
        when:
            def response = http.postForEntity(URI.create("/tables/participation"), joinToOtherTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.params == ["clientId": clientThatJoinedTableId.value]
    }

    def "should not join client to reserved poker table when client doesn't have enough tokens to start biding"() {
        given:
            def clientThatReservedTableTokens = sampleTokens(count: 100)
            def clientThatReservedTableId = prepareClientWithTokens(clientThatReservedTableTokens)
        and:
            def initialBidingRate = sampleTokens(count: 100)
            def tableWithToHighBidingRateId = reservePokerTable(clientThatReservedTableId, initialBidingRate)
        and:
            def clientThatWantJoinTableTokens = sampleTokens(count: 50)
            def clientThatWantJoinTableId = prepareClientWithTokens(clientThatWantJoinTableTokens)
        and:
            def joinToTable = sampleJoinTable(tableId: tableWithToHighBidingRateId, clientId: clientThatWantJoinTableId)
        when:
            def response = http.postForEntity(URI.create("/tables/participation"), joinToTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == InitialBidingRateTooHigh.CODE
            exceptionView.params == [
                    "clientId": clientThatWantJoinTableId.value,
                    "tableId": tableWithToHighBidingRateId.value,
                    "tokens": clientThatWantJoinTableTokens.count.toString(),
                    "initialBidingRate": initialBidingRate.count.toString()
            ]
    }

    def "should not join client when max table participants count is reached"() {
        given:
            def clientThatReservedTableId = prepareClient()
        and:
            def reservedTableId = reserveTable(clientThatReservedTableId)
        and:
            (0..9).forEach{ joinTable(reservedTableId, prepareClient()) }
        and:
            def clientThatWantJoinTableId = prepareClient()
        and:
            def joinToTable = sampleJoinTable(tableId: reservedTableId, clientId: clientThatWantJoinTableId)
        when:
            def response = http.postForEntity(URI.create("/tables/participation"), joinToTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == TableFull.CODE
            exceptionView.params == [
                    "clientId": clientThatWantJoinTableId.value,
                    "tableId": reservedTableId.value,
                    "maxParticipantsCount": "10"
            ]
    }
}

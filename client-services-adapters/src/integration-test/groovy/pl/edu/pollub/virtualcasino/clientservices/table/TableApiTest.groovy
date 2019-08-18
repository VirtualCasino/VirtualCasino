package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pollub.virtualcasino.clientservices.ClientServicesApiTest
import pl.edu.pollub.virtualcasino.clientservices.ExceptionView
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.InitialBidingRateTooHigh
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableFull
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedJoinedTableListener
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedRouletteTableReservedListener

import static org.springframework.http.HttpStatus.*
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReserveRouletteTable

class TableApiTest extends ClientServicesApiTest {

    @Autowired
    TableEventPublisher eventPublisher

    def "should reserve table"() {
        given:
            def clientId = setupClient()
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        and:
            def tableReservedListener = new FakedRouletteTableReservedListener()
            eventPublisher.subscribe(tableReservedListener)
        when:
            def tableId = reserveTable(clientId)
        then:
            tableRepository.containsWithParticipation(expectedParticipation)
        and:
            conditions.eventually {
                def event = tableReservedListener.listenedEvents.first()
                event.clientId == clientId
                event.tableId == tableId
            }
        cleanup:
            eventPublisher.unsubscribe(tableReservedListener)
    }

    def "should join client to reserved table"() {
        given:
            def clientThatReservedTableId = setupClient()
        and:
            def reservedTableId = reserveTable(clientThatReservedTableId)
        and:
            def clientThatWantJoinTableId = setupClient()
        and:
            def expectedParticipation = sampleParticipation(clientId: clientThatWantJoinTableId)
        and:
            def joinedTableListener = new FakedJoinedTableListener()
            eventPublisher.subscribe(joinedTableListener)
        when:
            joinTable(reservedTableId, clientThatWantJoinTableId)
        then:
            tableRepository.containsWithParticipation(expectedParticipation)
        and:
            conditions.eventually {
                def event = joinedTableListener.listenedEvents.first()
                event.clientId == clientThatWantJoinTableId
                event.tableId == reservedTableId
            }
        cleanup:
            eventPublisher.unsubscribe(joinedTableListener)
    }

    def "should not reserve table multiple times by the same client"() {
        given:
            def clientThatReservedTableId = setupClient()
        and:
            reserveTable(clientThatReservedTableId)
        and:
            def reserveTable = sampleReserveRouletteTable(clientId: clientThatReservedTableId)
        when:
            def response = http.postForEntity(URI.create("/casino-services/tables/roulette"), reserveTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.params == ["clientId": clientThatReservedTableId.value.toString()]
    }

    def "should not join client to table when client already reserved other table"() {
        given:
            def clientThatReservedTableId = setupClient()
        and:
            reserveTable(clientThatReservedTableId)
        and:
            def otherClientThatReservedOtherTableId = setupClient()
        and:
            def otherReservedTableId = reserveTable(otherClientThatReservedOtherTableId)
        and:
            def joinOtherTable = sampleJoinTable(clientId: clientThatReservedTableId, tableId: otherReservedTableId)
        when:
            def response = http.postForEntity(URI.create("/casino-services/tables/participation"), joinOtherTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.params == ["clientId": clientThatReservedTableId.value.toString()]
    }

    def "should not join client to reserved table when client already joined to other table"() {
        given:
            def clientThatReservedTableId = setupClient()
        and:
            def tableId = reserveTable(clientThatReservedTableId)
        and:
            def clientThatReservedOtherTableId = setupClient()
        and:
            def otherTableId = reserveTable(clientThatReservedOtherTableId)
        and:
            def clientThatJoinedTableId = setupClient()
        and:
            joinTable(tableId, clientThatJoinedTableId)
        and:
            def joinToOtherTable = sampleJoinTable(tableId: otherTableId, clientId: clientThatJoinedTableId)
        when:
            def response = http.postForEntity(URI.create("/casino-services/tables/participation"), joinToOtherTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.params == ["clientId": clientThatJoinedTableId.value.toString()]
    }

    def "should not join client to reserved poker table when client doesn't have enough tokens to start biding"() {
        given:
            def clientThatReservedTableTokens = sampleTokens(count: 100)
            def clientThatReservedTableId = setupClientWithTokens(clientThatReservedTableTokens)
        and:
            def initialBidingRate = sampleTokens(count: 100)
            def tableWithToHighBidingRateId = reservePokerTable(clientThatReservedTableId, initialBidingRate)
        and:
            def clientThatWantJoinTableTokens = sampleTokens(count: 50)
            def clientThatWantJoinTableId = setupClientWithTokens(clientThatWantJoinTableTokens)
        and:
            def joinToTable = sampleJoinTable(tableId: tableWithToHighBidingRateId, clientId: clientThatWantJoinTableId)
        when:
            def response = http.postForEntity(URI.create("/casino-services/tables/participation"), joinToTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == InitialBidingRateTooHigh.CODE
            exceptionView.params == [
                    "clientId": clientThatWantJoinTableId.value.toString(),
                    "tableId": tableWithToHighBidingRateId.value.toString(),
                    "tokens": clientThatWantJoinTableTokens.count.toString(),
                    "initialBidingRate": initialBidingRate.count.toString()
            ]
    }

    def "should not join client when max table participants count is reached"() {
        given:
            def clientThatReservedTableId = setupClient()
        and:
            def reservedTableId = reserveTable(clientThatReservedTableId)
        and:
            (0..8).forEach{ joinTable(reservedTableId, setupClient()) }
        and:
            def clientThatWantJoinTableId = setupClient()
        and:
            def joinToTable = sampleJoinTable(tableId: reservedTableId, clientId: clientThatWantJoinTableId)
        when:
            def response = http.postForEntity(URI.create("/casino-services/tables/participation"), joinToTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == TableFull.CODE
            exceptionView.params == [
                    "clientId": clientThatWantJoinTableId.value.toString(),
                    "tableId": reservedTableId.value.toString(),
                    "maxParticipantsCount": "10"
            ]
    }
}

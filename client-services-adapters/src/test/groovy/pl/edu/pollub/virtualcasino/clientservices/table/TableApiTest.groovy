package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pollub.virtualcasino.ExceptionView
import pl.edu.pollub.virtualcasino.clientservices.ClientServicesApiTest
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.InitialBidingRateTooHigh
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableFull
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedJoinedTableListener
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedRouletteTableReservedListener

import static org.springframework.http.HttpStatus.*
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReserveRouletteTable

class TableApiTest extends ClientServicesApiTest {

    @Autowired
    TableEventPublisher eventPublisher

    def "should reserve table"() {
        given:
            def client = setupClient()
        and:
            def expectedParticipation = sampleParticipation(clientId: client.id())
        and:
            def tableReservedListener = new FakedRouletteTableReservedListener()
            eventPublisher.subscribe(tableReservedListener)
        when:
            def tableId = reserveTable(client.id())
        then:
            tableRepository.containsWithParticipation(expectedParticipation)
        and:
            conditions.eventually {
                def event = tableReservedListener.listenedEvents.first()
                event.clientId == client.id()
                event.tableId == tableId
                event.clientTokens == client.tokens()
                event.firstPlayerNick == client.nick()
            }
        cleanup:
            eventPublisher.unsubscribe(tableReservedListener)
    }

    def "should join client to reserved table"() {
        given:
            def clientThatReservedTable = setupClient()
        and:
            def reservedTableId = reserveTable(clientThatReservedTable.id())
        and:
            def clientThatWantJoiningTable = setupClient()
        and:
            def expectedParticipation = sampleParticipation(clientId: clientThatWantJoiningTable.id())
        and:
            def joinedTableListener = new FakedJoinedTableListener()
            eventPublisher.subscribe(joinedTableListener)
        when:
            joinTable(reservedTableId, clientThatWantJoiningTable.id())
        then:
            tableRepository.containsWithParticipation(expectedParticipation)
        and:
            conditions.eventually {
                def event = joinedTableListener.listenedEvents.first()
                event.clientId == clientThatWantJoiningTable.id()
                event.tableId == reservedTableId
                event.clientTokens == clientThatWantJoiningTable.tokens()
            }
        cleanup:
            eventPublisher.unsubscribe(joinedTableListener)
    }

    def "should not reserve table multiple times by the same client"() {
        given:
            def clientThatReservedTable = setupClient()
        and:
            reserveTable(clientThatReservedTable.id())
        and:
            def reserveTable = sampleReserveRouletteTable(clientId: clientThatReservedTable.id())
        when:
            def response = http.postForEntity(URI.create("/virtual-casino/casino-services/tables/roulette"), reserveTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.param == ["clientId": clientThatReservedTable.id().value.toString()]
    }

    def "should not join client to table when client already reserved other table"() {
        given:
            def clientThatReservedTable = setupClient()
            def clientThatReservedTableId = clientThatReservedTable.id()
        and:
            reserveTable(clientThatReservedTableId)
        and:
            def otherClientThatReservedOtherTable = setupClient()
            def otherClientThatReservedOtherTableId = otherClientThatReservedOtherTable.id()
        and:
            def otherReservedTableId = reserveTable(otherClientThatReservedOtherTableId)
        and:
            def joinOtherTable = sampleJoinTable(clientId: clientThatReservedTableId, tableId: otherReservedTableId)
        when:
            def response = http.postForEntity(URI.create("/virtual-casino/casino-services/tables/participation"), joinOtherTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.param == ["clientId": clientThatReservedTableId.value.toString()]
    }

    def "should not join client to reserved table when client already joined to other table"() {
        given:
            def clientThatReservedTable = setupClient()
        and:
            def tableId = reserveTable(clientThatReservedTable.id())
        and:
            def clientThatReservedOtherTable = setupClient()
        and:
            def otherTableId = reserveTable(clientThatReservedOtherTable.id())
        and:
            def clientThatJoinedTable = setupClient()
        and:
            joinTable(tableId, clientThatJoinedTable.id())
        and:
            def joinToOtherTable = sampleJoinTable(tableId: otherTableId, clientId: clientThatJoinedTable.id())
        when:
            def response = http.postForEntity(URI.create("/virtual-casino/casino-services/tables/participation"), joinToOtherTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.param == ["clientId": clientThatJoinedTable.id().value.toString()]
    }

    def "should not join client to reserved poker table when client doesn't have enough tokens to start biding"() {
        given:
            def clientThatReservedTableTokens = sampleTokens(count: 100)
            def clientThatReservedTable = setupClientWithTokens(clientThatReservedTableTokens)
        and:
            def initialBidingRate = sampleTokens(count: 200)
            def tableWithToHighBidingRateId = reservePokerTable(clientThatReservedTable.id(), initialBidingRate)
        and:
            def clientThatWantJoinTableTokens = sampleTokens(count: 50)
            def clientThatWantJoinTable = setupClientWithTokens(clientThatWantJoinTableTokens)
        and:
            def joinToTable = sampleJoinTable(tableId: tableWithToHighBidingRateId, clientId: clientThatWantJoinTable.id())
        when:
            def response = http.postForEntity(URI.create("/virtual-casino/casino-services/tables/participation"), joinToTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == InitialBidingRateTooHigh.CODE
            exceptionView.param == [
                    "clientId": clientThatWantJoinTable.id().value.toString(),
                    "tokens": clientThatWantJoinTable.tokens().count.toString(),
                    "initialBidingRate": initialBidingRate.count.toString()
            ]
    }

    def "should not join client when max table participants count is reached"() {
        given:
            def clientThatReservedTable = setupClient()
        and:
            def reservedTableId = reserveTable(clientThatReservedTable.id())
        and:
            (0..8).forEach{ joinTable(reservedTableId, setupClient().id()) }
        and:
            def clientThatWantJoinTable = setupClient()
        and:
            def joinToTable = sampleJoinTable(tableId: reservedTableId, clientId: clientThatWantJoinTable.id())
        when:
            def response = http.postForEntity(URI.create("/virtual-casino/casino-services/tables/participation"), joinToTable, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == TableFull.CODE
            exceptionView.param == [
                    "clientId": clientThatWantJoinTable.id().value.toString(),
                    "tableId": reservedTableId.value.toString(),
                    "maxParticipantsCount": "10"
            ]
    }
}

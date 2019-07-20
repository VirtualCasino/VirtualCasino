package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import pl.edu.pollub.virtualcasino.clientservices.CasinoServicesBoundedContext
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.InitialBidingRateTooHigh
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.TableFull
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import static org.springframework.http.HttpStatus.*
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.events.SampleTokensCountIncreased.sampleTokensCountIncreased
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType.*
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands.SampleReserveTable.sampleReserveTable

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

    TableId uriToId(URI tableUri) {
        def tableUriSegments = tableUri.toString().split("/")
        def tableIdValue = tableUriSegments[tableUriSegments.length - 1]
        return sampleTableId(value: tableIdValue)
    }

    ClientId prepareClient() {
        def clientThatReservedTableId = sampleClientId()
        def clientThatReservedTable = sampleClient(id: clientThatReservedTableId)
        clientRepository.add(clientThatReservedTable)
        return clientThatReservedTableId
    }

    ClientId prepareClientWithTokens(Tokens tokens) {
        def clientThatReservedTableId = sampleClientId()
        def clientThatReservedTable = sampleClient(id: clientThatReservedTableId,
                changes: [sampleTokensCountIncreased(clientId: clientThatReservedTableId, tokens: tokens)])
        clientRepository.add(clientThatReservedTable)
        return clientThatReservedTableId
    }

    TableId reserveTable(ClientId clientThatReservedTableId) {
        def tableUri = http.postForLocation(URI.create("/tables"), sampleReserveTable(clientId: clientThatReservedTableId))
        return uriToId(tableUri)
    }

    TableId reservePokerTable(ClientId clientThatReservedTableId, Tokens initialBidingRate) {
        def reserveTable = sampleReserveTable(clientId: clientThatReservedTableId, gameType: POKER, initialBidingRate: initialBidingRate)
        def tableUri = http.postForLocation(URI.create("/tables"), reserveTable)
        return uriToId(tableUri)
    }

    TableId joinTable(TableId tableId, ClientId clientThatJoinedToTableId) {
        def tableUri = http.postForLocation(URI.create("/tables/participation"), sampleJoinTable(tableId: tableId, clientId: clientThatJoinedToTableId))
        return uriToId(tableUri)
    }
}

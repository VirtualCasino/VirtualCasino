package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedTableRepository
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClient.*
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.comands.SampleBuyTokens.sampleBuyTokens
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.events.SampleTokensBought.sampleTokensBought
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleJoinedTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTable.sampleTable
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleRouletteGameLeft.sampleRouletteGameLeft

class ClientTest extends Specification {

    def tableRepository = new FakedTableRepository()

    @Subject
    def client

    def setup() {
        tableRepository.clear()
    }

    def "should buy tokens"() {
        given:
            client = sampleClient()
        and:
            def buyTokens = sampleBuyTokens(tokens: sampleTokens(count: 100))
        when:
            client.handle(buyTokens)
        then:
            client.tokens == sampleTokens(count: 100)
    }

    @Unroll
    def "should throw TokensCountMustBePositive when client try to buy tokens count: #invalidTokensCount"() {
        given:
            def clientId = sampleClientId()
            client = sampleClient(id: clientId)
        and:
            def tokens = sampleTokens(count: invalidTokensCount)
            def buyTokens = sampleBuyTokens(tokens: tokens)
        when:
            client.handle(buyTokens)
        then:
            def e = thrown(TokensCountMustBePositive)
            e.clientId == clientId
            e.tokens == tokens
        where:
            invalidTokensCount << [0, -50]
    }

    def "should throw ClientBusy when client reserved table and try to buy tokens"() {
        given:
            def clientId = sampleClientId()
            client = sampleClient(id: clientId, tableRepository: tableRepository)
        and:
            def tableId = sampleTableId()
            def tableReserved = sampleRouletteTableReserved(tableId: tableId, clientId: clientId)
            def table = sampleTable(id: tableId, changes: [tableReserved])
            tableRepository.add(table)
        and:
            def buyTokens = sampleBuyTokens(tokens: sampleTokens(count: 100))
        when:
            client.handle(buyTokens)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw ClientBusy when client joined table and try to buy tokens"() {
        given:
            def clientId = sampleClientId()
            client = sampleClient(id: clientId, tableRepository: tableRepository)
        and:
            def tableId = sampleTableId()
            def otherClientId = sampleClientId()
            def table = sampleTable(id: tableId, changes: [
                    sampleRouletteTableReserved(tableId: tableId, clientId: otherClientId),
                    sampleJoinedTable(tableId: tableId, clientId: clientId)
            ])
            tableRepository.add(table)
        and:
            def buyTokens = sampleBuyTokens(tokens: sampleTokens(count: 100))
        when:
            client.handle(buyTokens)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should charge client when he left game"() {
        given:
            def clientThatLeftGameId = sampleClientId()
            def tokensBeforePlayingGame = sampleTokens(count: 50)
            def boughtTokens = sampleTokensBought(tokens: tokensBeforePlayingGame)
            client = sampleClient(id: clientThatLeftGameId, changes: [boughtTokens])
        and:
            def playerThatLeftGameId = sampleRoulettePlayerId(value: clientThatLeftGameId.value)
            def tokensAfterPlayingGame = sampleTokens(count: 100)
            def gameLeft = sampleRouletteGameLeft(playerId: playerThatLeftGameId, playerTokens: tokensAfterPlayingGame)
        when:
            client.when(gameLeft)
        then:
            client.tokens() == sampleTokens(count: 100)
    }

}

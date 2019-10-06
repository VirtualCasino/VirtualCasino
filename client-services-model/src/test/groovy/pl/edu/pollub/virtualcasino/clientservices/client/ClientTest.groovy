package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotRegistered
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedClientEventPublisher
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedClientRegisteredListener
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedTokensBoughtListener
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedTableRepository
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClient.*
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleNick.sampleNick
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.comands.SampleRegisterClient.sampleRegisterClient
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.comands.SampleBuyTokens.sampleBuyTokens
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.events.SampleClientRegistered.sampleClientRegistered
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

    def "should register client with nick"() {
        given:
            client = sampleClient()
        and:
            def nick = sampleNick(value: "Test")
            def registerClient = sampleRegisterClient(nick: nick)
        when:
            client.handle(registerClient)
        then:
            client.nick() == nick
    }

    def "should publish that client is registered"() {
        given:
            def eventPublisher = new FakedClientEventPublisher()
            def clientRegisteredListener = new FakedClientRegisteredListener()
            eventPublisher.subscribe(clientRegisteredListener)
            client = sampleClient(eventPublisher: eventPublisher)
        and:
            def nick = sampleNick(value: "Test")
            def registerClient = sampleRegisterClient(nick: nick)
        when:
            client.handle(registerClient)
        then:
            def listenedEvent = clientRegisteredListener.listenedEvents.first()
            with(listenedEvent) {
                clientId == client.id()
            }
    }

    def "should buy tokens when client is registered"() {
        given:
            def clientRegistered = sampleClientRegistered()
            client = sampleClient(changes: [clientRegistered])
        and:
            def buyTokens = sampleBuyTokens(tokens: sampleTokens(count: 100))
        when:
            client.handle(buyTokens)
        then:
            client.tokens == sampleTokens(count: 100)
    }

    def "should publish that registered client bought tokens"() {
        given:
            def eventPublisher = new FakedClientEventPublisher()
            def tokensBoughtListener = new FakedTokensBoughtListener()
            eventPublisher.subscribe(tokensBoughtListener)
            client = sampleClient(eventPublisher: eventPublisher, changes: [sampleClientRegistered()])
        and:
            def buyTokens = sampleBuyTokens(tokens: sampleTokens(count: 100))
        when:
            client.handle(buyTokens)
        then:
            def listenedEvent = tokensBoughtListener.listenedEvents.first()
            with(listenedEvent) {
                clientId == client.id()
                tokens == sampleTokens(count: 100)
            }
    }

    def "should throw ClientNotRegistered when client try to buy tokens and is not registered"() {
        given:
            client = sampleClient()
        and:
            def tryBuyTokens = sampleBuyTokens(tokens: sampleTokens(count: 100))
        when:
            client.handle(tryBuyTokens)
        then:
            def e = thrown(ClientNotRegistered)
            e.clientId == client.id
    }

    @Unroll
    def "should throw TokensCountMustBePositive when registered client try to buy tokens count: #invalidTokensCount"() {
        given:
            def clientId = sampleClientId()
            client = sampleClient(id: clientId, changes: [sampleClientRegistered()])
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

    def "should throw ClientBusy when registered client reserved table and try to buy tokens"() {
        given:
            def clientId = sampleClientId()
            client = sampleClient(id: clientId, tableRepository: tableRepository, changes: [sampleClientRegistered()])
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

    def "should throw ClientBusy when registered client joined table and try to buy tokens"() {
        given:
            def clientId = sampleClientId()
            client = sampleClient(id: clientId, tableRepository: tableRepository, changes: [sampleClientRegistered()])
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

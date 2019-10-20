package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.RequestEntity
import pl.edu.pollub.virtualcasino.ExceptionView
import pl.edu.pollub.virtualcasino.clientservices.ClientServicesApiTest
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedClientRegisteredListener
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedTokensBoughtListener

import static org.springframework.http.HttpMethod.*
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleNick.sampleNick
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.comands.SampleBuyTokens.sampleBuyTokens

class ClientApiTest extends ClientServicesApiTest {

    @Autowired
    ClientEventPublisher eventPublisher

    def "should register client"() {
        given:
            def nick = sampleNick(value: "Test")
        and:
            def clientRegisteredListener = new FakedClientRegisteredListener()
            eventPublisher.subscribe(clientRegisteredListener)
        when:
            def registeredClientId = registerClient(nick)
        then:
            def registeredClient = clientRepository.find(registeredClientId)
            registeredClient != null
            registeredClient.nick() == nick
        and:
            conditions.eventually {
                def event = clientRegisteredListener.listenedEvents.first()
                event.clientId == registeredClientId
                event.nick == nick
            }
        cleanup:
            eventPublisher.unsubscribe(clientRegisteredListener)
    }

    def "should buy tokens"() {
        given:
            def clientThatBuyingTokens = setupClient()
            def tokens = sampleTokens(count: 100)
            def buyTokens = sampleBuyTokens(clientId: clientThatBuyingTokens.id(), tokens: tokens)
        and:
            def tokensBoughtListener = new FakedTokensBoughtListener()
            eventPublisher.subscribe(tokensBoughtListener)
        when:
            http.put(URI.create("/virtual-casino/casino-services/clients/tokens"), buyTokens)
        then:
            def foundClient = clientRepository.find(clientThatBuyingTokens.id())
            foundClient.tokens() == tokens
        and:
            conditions.eventually {
                def event = tokensBoughtListener.listenedEvents.first()
                event.clientId == clientThatBuyingTokens.id()
                event.tokens == tokens
            }
        cleanup:
            eventPublisher.unsubscribe(tokensBoughtListener)
    }

    def "should not buy tokens when client reserved table"() {
        given:
            def clientThatReservedTable = setupClient()
            reserveTable(clientThatReservedTable.id())
        and:
            def tokens = sampleTokens(count: 100)
            def buyTokens = sampleBuyTokens(clientId: clientThatReservedTable.id(), tokens: tokens)
            def request = new RequestEntity<BuyTokens>(buyTokens, PUT, URI.create("/virtual-casino/casino-services/clients/tokens"))
        when:
            def response = http.exchange(request, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.param == ["clientId": clientThatReservedTable.id().value.toString()]
    }

    def "should not buy tokens when client joined to reserved table"() {
        given:
            def clientThatJoinedTable = setupClient()
        and:
            def clientThatReservedTable = setupClient()
            def tableId = reserveTable(clientThatReservedTable.id())
            joinTable(tableId, clientThatJoinedTable.id())
        and:
            def tokens = sampleTokens(count: 100)
            def buyTokens = sampleBuyTokens(clientId: clientThatJoinedTable.id(), tokens: tokens)
            def request = new RequestEntity<BuyTokens>(buyTokens, PUT, URI.create("/virtual-casino/casino-services/clients/tokens"))
        when:
            def response = http.exchange(request, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.param == ["clientId": clientThatJoinedTable.id().value.toString()]
    }
}

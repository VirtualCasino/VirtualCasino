package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.RequestEntity
import pl.edu.pollub.virtualcasino.ExceptionView
import pl.edu.pollub.virtualcasino.clientservices.ClientServicesApiTest
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedTokensBoughtListener

import static org.springframework.http.HttpMethod.*
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.comands.SampleBuyTokens.sampleBuyTokens

class ClientApiTest extends ClientServicesApiTest {

    @Autowired
    ClientEventPublisher eventPublisher

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
            exceptionView.params == ["clientId": clientThatJoinedTable.id().value.toString()]
    }
}

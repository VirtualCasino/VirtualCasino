package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.RequestEntity
import pl.edu.pollub.virtualcasino.clientservices.ClientServicesApiTest
import pl.edu.pollub.virtualcasino.clientservices.ExceptionView
import pl.edu.pollub.virtualcasino.clientservices.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedTokensBoughtListener

import static org.springframework.http.HttpMethod.*
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.comands.SampleBuyTokens.sampleBuyTokens

class ClientApiTest extends ClientServicesApiTest {

    @Autowired
    ClientEventPublisher eventPublisher

    def "should buy tokens"() {
        given:
            def clientThatWantBuyTokensId = setupClient()
            def tokens = sampleTokens(count: 100)
            def buyTokens = sampleBuyTokens(clientId: clientThatWantBuyTokensId, tokens: tokens)
        and:
            def tokensBoughtListener = new FakedTokensBoughtListener()
            eventPublisher.subscribe(tokensBoughtListener)
        when:
            http.put(URI.create("/casino-services/clients/tokens"), buyTokens)
        then:
            def foundClient = clientRepository.find(clientThatWantBuyTokensId)
            foundClient.tokens() == tokens
        and:
            conditions.eventually {
                def event = tokensBoughtListener.listenedEvents.first()
                event.clientId == clientThatWantBuyTokensId
                event.tokens == tokens
            }
        cleanup:
            eventPublisher.unsubscribe(tokensBoughtListener)
    }

    def "should not buy tokens when client joined to reserved table"() {
        given:
            def clientThatJoinedTableId = setupClient()
        and:
            def clientThatReservedTableId = setupClient()
            def tableId = reserveTable(clientThatReservedTableId)
            joinTable(tableId, clientThatJoinedTableId)
        and:
            def tokens = sampleTokens(count: 100)
            def buyTokens = sampleBuyTokens(clientId: clientThatJoinedTableId, tokens: tokens)
            def request = new RequestEntity<BuyTokens>(buyTokens, PUT, URI.create("/casino-services/clients/tokens"))
        when:
            def response = http.exchange(request, ExceptionView.class)
        then:
            response.statusCode == BAD_REQUEST
            def exceptionView = response.body
            exceptionView.code == ClientBusy.CODE
            exceptionView.params == ["clientId": clientThatJoinedTableId.value.toString()]
    }
}

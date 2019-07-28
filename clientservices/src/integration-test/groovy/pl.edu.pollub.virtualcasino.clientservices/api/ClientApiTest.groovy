package pl.edu.pollub.virtualcasino.clientservices.api

import org.springframework.http.RequestEntity
import pl.edu.pollub.virtualcasino.clientservices.domain.client.commands.BuyTokens
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientBusy

import static org.springframework.http.HttpMethod.*
import static org.springframework.http.HttpStatus.BAD_REQUEST
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.comands.SampleBuyTokens.sampleBuyTokens

class ClientApiTest extends ClientServicesApiTest {

    def "should buy tokens"() {
        given:
            def clientThatWantBuyTokensId = prepareClient()
            def tokens = sampleTokens(count: 100)
            def buyTokens = sampleBuyTokens(clientId: clientThatWantBuyTokensId, tokens: tokens)
        when:
            http.put(URI.create("/casino-services/clients/tokens"), buyTokens)
        then:
            def foundClient = clientRepository.find(clientThatWantBuyTokensId)
            foundClient.tokens() == tokens
    }

    def "should not buy tokens when client joined to reserved table"() {
        given:
            def clientThatJoinedTableId = prepareClient()
        and:
            def clientThatReservedTableId = prepareClient()
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
            exceptionView.params == ["clientId": clientThatJoinedTableId.value]
    }
}

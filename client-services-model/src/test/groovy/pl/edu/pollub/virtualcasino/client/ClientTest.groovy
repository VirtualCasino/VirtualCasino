package pl.edu.pollub.virtualcasino.client

import pl.edu.pollub.virtualcasino.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.client.exceptions.TokensCountMustBePositive
import pl.edu.pollub.virtualcasino.table.fakes.FakedTableRepository
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.*
import static pl.edu.pollub.virtualcasino.client.samples.comands.SampleBuyTokens.sampleBuyTokens
import static pl.edu.pollub.virtualcasino.table.samples.SampleTable.sampleTable
import static pl.edu.pollub.virtualcasino.table.samples.SampleTable.sampleTableId
import static pl.edu.pollub.virtualcasino.table.samples.events.SampleJoinedToTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.table.samples.events.SampleTableReserved.sampleRouletteTableReserved

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

}

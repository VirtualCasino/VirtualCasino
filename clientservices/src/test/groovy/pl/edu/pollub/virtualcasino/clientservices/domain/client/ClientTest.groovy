package pl.edu.pollub.virtualcasino.clientservices.domain.client

import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableFactory
import pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes.FakedTableRepository
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.comands.SampleIncreaseTokensCount.sampleIncreaseTokensCount
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTable

class ClientTest extends Specification {

    def tableFactory = Stub(TableFactory)
    def tableRepository = new FakedTableRepository()

    @Subject
    def client

    def setup() {
        tableFactory.create(*_) >> sampleTable()
        tableRepository.clear()
    }

    def "should increase client tokens count"() {
        given:
            client = sampleClient()
        and:
            def increaseTokensCount = sampleIncreaseTokensCount(tokens: sampleTokens(count: 100))
        when:
            client.handle(increaseTokensCount)
        then:
            client.tokens == sampleTokens(count: 100)
    }

    //TODO: testy corner cases dla ilości tokenów

}

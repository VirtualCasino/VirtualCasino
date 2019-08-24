package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pollub.virtualcasino.BaseIntegrationTest
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayer.sampleRoulettePlayer

class RouletteTableReservedListenerTest extends BaseIntegrationTest {

    @Subject
    @Autowired
    RouletteTableReservedListener listener

    def cleanup() {
        repository.clear()
    }

    def "should create roulette game for reserved table"() {
        given:
            def tableId = sampleTableId()
            def clientId = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(tableId: tableId, clientId: clientId)
        and:
            def expectedPlayer = sampleRoulettePlayer(clientId: clientId)
        when:
            listener.reactTo(tableReserved)
        then:
            def foundRouletteGame = repository.find(sampleRouletteGameId(value: tableId.value))
            foundRouletteGame != null
            def players = foundRouletteGame.players()
            players.size() == 1
            with(players.last()) {
                id() == expectedPlayer.id()
                tokens() == expectedPlayer.tokens()
            }
    }

}

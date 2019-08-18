package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleJoinedToTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGame.sampleRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayer.sampleRoulettePlayer

class JoinedTableListenerTest extends BaseIntegrationTest {

    @Subject
    @Autowired
    JoinedTableListener listener

    @Autowired
    RouletteGameRepository repository

    def "should roulette game has player that joined table"() {
        given:
            def rouletteGameId = sampleRouletteGameId()
            def rouletteGame = sampleRouletteGame(id: rouletteGameId)
            repository.add(rouletteGame)
        and:
            def clientId = sampleClientId()
            def tableId = sampleTableId(value: rouletteGameId.value)
            def joinedTable = sampleJoinedTable(tableId: tableId, clientId: clientId)
        and:
            def expectedPlayer = sampleRoulettePlayer(clientId: clientId)
        when:
            listener.reactTo(joinedTable)
        then:
            def foundRouletteGame = repository.find(sampleRouletteGameId(value: tableId.value))
            foundRouletteGame != null
            foundRouletteGame.players.contains(expectedPlayer)
    }

}

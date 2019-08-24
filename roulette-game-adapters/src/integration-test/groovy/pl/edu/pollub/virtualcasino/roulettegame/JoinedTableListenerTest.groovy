package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
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

    def cleanup() {
        repository.clear()
    }

    def "should roulette game has player that joined table"() {
        given:
            def rouletteGameId = sampleRouletteGameId()
            def rouletteGame = sampleRouletteGame(id: rouletteGameId)
            repository.add(rouletteGame)
        and:
            def clientId = sampleClientId()
            def tableId = sampleTableId(value: rouletteGameId.value)
            def clientTokens = sampleTokens(count: 50)
            def joinedTable = sampleJoinedTable(tableId: tableId, clientId: clientId, clientTokens: clientTokens)
        and:
            def expectedPlayer = sampleRoulettePlayer(clientId: clientId, tokens: clientTokens)
        when:
            listener.reactTo(joinedTable)
        then:
            def foundRouletteGame = repository.find(sampleRouletteGameId(value: tableId.value))
            foundRouletteGame != null
            def players = foundRouletteGame.players()
            players.size() == 1
            with(players.first()) {
                id() == expectedPlayer.id()
                tokens() == expectedPlayer.tokens()
            }
    }

}

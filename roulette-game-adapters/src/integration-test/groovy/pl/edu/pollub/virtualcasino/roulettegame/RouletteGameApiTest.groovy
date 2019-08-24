package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.RequestEntity
import pl.edu.pollub.virtualcasino.roulettegame.commands.LeaveRouletteGame
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGameLeftListener
import pl.edu.pollub.virtualcasino.BaseIntegrationTest

import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpStatus.*
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleJoinedToTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleLeaveRouletteGame.sampleLeaveRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class RouletteGameApiTest extends BaseIntegrationTest {

    @Autowired
    RouletteGameEventPublisher eventPublisher

    @Autowired
    RouletteTableReservedListener reservedListener

    @Autowired
    JoinedTableListener joinedTableListener

    def cleanup() {
        repository.clear()
    }

    def "should leave game"() {
        given:
            def tableId = sampleTableId()
            def clientThatReservedTableId = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(tableId: tableId, clientId: clientThatReservedTableId)
            reservedListener.reactTo(tableReserved)
        and:
            def clientId = sampleClientId()
            def clientTokens = sampleTokens(count: 50)
            def joinedTable = sampleJoinedTable(tableId: tableId, clientId: clientId, clientTokens: clientTokens)
            joinedTableListener.reactTo(joinedTable)
        and:
            def rouletteGameLeftListener = new FakedRouletteGameLeftListener()
            eventPublisher.subscribe(rouletteGameLeftListener)
        and:
            def playerId = sampleRoulettePlayerId(value: clientId.value)
            def gameId = sampleRouletteGameId(value: tableId.value)
            def leaveGame = sampleLeaveRouletteGame(gameId: gameId, playerId: playerId)
            def request = new RequestEntity<LeaveRouletteGame>(leaveGame, DELETE, URI.create("/virtual-casino/roulette-game/games/players"))
        when:
            def response = http.exchange(request, Void.class)
        then:
            response.statusCode == NO_CONTENT
        and:
            def foundRouletteGame = repository.find(sampleRouletteGameId(value: tableId.value))
            foundRouletteGame != null
            def players = foundRouletteGame.players()
            players.size() == 1
        and:
            conditions.eventually {
                def event = rouletteGameLeftListener.listenedEvents.first()
                event.playerId == playerId
                event.gameId == gameId
                event.playerTokens == clientTokens
            }
    }

}

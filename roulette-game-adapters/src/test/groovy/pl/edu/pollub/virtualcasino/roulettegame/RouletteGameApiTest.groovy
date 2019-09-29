package pl.edu.pollub.virtualcasino.roulettegame

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.RequestEntity
import pl.edu.pollub.virtualcasino.FakedClock
import pl.edu.pollub.virtualcasino.FakedRandom
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.roulettegame.commands.LeaveRouletteGame
import pl.edu.pollub.virtualcasino.roulettegame.commands.PlaceRouletteBet
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGameLeftListener
import pl.edu.pollub.virtualcasino.BaseIntegrationTest

import static org.springframework.http.HttpMethod.DELETE
import static org.springframework.http.HttpMethod.POST
import static org.springframework.http.HttpStatus.*
import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleJoinedTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.roulettegame.ColorField.*
import static pl.edu.pollub.virtualcasino.roulettegame.PairField.*
import static pl.edu.pollub.virtualcasino.roulettegame.QuarterFiled.QUARTER_1_2_4_5
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleLeaveRouletteGame.sampleLeaveRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SamplePlaceRouletteBet.samplePlaceRouletteBet

class RouletteGameApiTest extends BaseIntegrationTest {

    @Autowired
    RouletteGameEventPublisher eventPublisher

    @Autowired
    RouletteTableReservedListener reservedListener

    @Autowired
    JoinedTableListener joinedTableListener

    @Autowired
    FakedClock clock

    @Autowired
    FakedRandom random

    @Autowired
    SpinConfig spinConfig

    def cleanup() {
        repository.clear()
    }

    def "should leave game"() {
        given:
            def tableId = sampleTableId()
            def clientThatReservedTableId = sampleClientId()
            tableReserved(clientThatReservedTableId, tableId, sampleTokens(count: 50))
        and:
            def clientThatJoinedId = sampleClientId()
            def clientThatJoinedTableTokens = sampleTokens(count: 50)
            tableJoined(clientThatJoinedId, tableId, clientThatJoinedTableTokens)
        and:
            def rouletteGameLeftListener = new FakedRouletteGameLeftListener()
            eventPublisher.subscribe(rouletteGameLeftListener)
        and:
            def playerId = sampleRoulettePlayerId(value: clientThatJoinedId.value)
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
                event.playerTokens == clientThatJoinedTableTokens
            }
    }

    def "should provide sample spin for two players"() {
        given:
            def initialPointInTime = samplePointInTime()
            clock.moveTo(initialPointInTime)
        and:
            def clientThatReservedTableId = sampleClientId()
            def tableId = sampleTableId()
            def clientThatReservedTableTokensCount = 60
            tableReserved(clientThatReservedTableId, tableId, sampleTokens(count: clientThatReservedTableTokensCount))
        and:
            def clientThatJoinedTableId = sampleClientId()
            def clientThatJoinedTableTokensCount = 60
            tableJoined(clientThatJoinedTableId, tableId, sampleTokens(count: clientThatJoinedTableTokensCount))
        and:
            def gameId = sampleRouletteGameId(value: tableId.value)
            startSpin()
        and:
            def playerThatReservedTableId = sampleRoulettePlayerId(value: clientThatReservedTableId.value)
            def playerThatReservedTableBet1Value = 5
            betPlaced(gameId, playerThatReservedTableId, QUARTER_1_2_4_5, sampleTokens(count: playerThatReservedTableBet1Value))
            def playerThatReservedTableBet2Value = 7
            betPlaced(gameId, playerThatReservedTableId, RED, sampleTokens(count: playerThatReservedTableBet2Value))
        and:
            def playerThatJoinedTableId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def playerThatJoinedTableBetValue = 8
            betPlaced(gameId, playerThatJoinedTableId, PAIR_2_3, sampleTokens(count: playerThatJoinedTableBetValue))
        when:
            finishSpin(3)
        then:
            def foundRouletteGame = repository.find(gameId)
            foundRouletteGame != null
            def players = foundRouletteGame.players()
            players[0].tokens() == sampleTokens(count: clientThatReservedTableTokensCount - playerThatReservedTableBet1Value + playerThatReservedTableBet2Value * RED.valueMultiplier())
            players[1].tokens() == sampleTokens(count: clientThatJoinedTableTokensCount + playerThatJoinedTableBetValue * PAIR_2_3.valueMultiplier())
    }

    void tableReserved(ClientId clientId = sampleClientId(), TableId tableId = sampleTableId(), Tokens clientTokens = sampleTokens()) {
        def tableReserved = sampleRouletteTableReserved(
                tableId: tableId,
                clientId: clientId,
                clientTokens: clientTokens
        )
        reservedListener.reactTo(tableReserved)
    }

    void tableJoined(ClientId clientId = sampleClientId(), TableId tableId = sampleTableId(), Tokens clientTokens = sampleTokens()) {
        def tableJoined = sampleJoinedTable(
                tableId: tableId,
                clientId: clientId,
                clientTokens: clientTokens
        )
        joinedTableListener.reactTo(tableJoined)
    }

    void startSpin() {
        clock.moveTo(clock.instant().plusMillis(spinConfig.startSpinDelayAfterTableReservationInMilliseconds))
    }

    void betPlaced(RouletteGameId gameId, RoulettePlayerId playerId, RouletteField field, Tokens betValue) {
        def bet = samplePlaceRouletteBet(
                gameId: gameId,
                playerId: playerId,
                field: field,
                value: betValue
        )
        http.exchange(new RequestEntity<PlaceRouletteBet>(
                bet,
                POST,
                URI.create("/virtual-casino/roulette-game/games/bets")), Void.class)
    }

    void finishSpin(Integer drawnFieldNumber) {
        random.setRandomIntValue(drawnFieldNumber)
        clock.moveTo(clock.instant().plusMillis(spinConfig.spinTimeDurationInMilliseconds))
    }
}

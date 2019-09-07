package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.roulettegame.exceptions.RoulettePlayerNotExist
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGameLeftListener
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGamePublisher
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleJoinedTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.roulettegame.RouletteField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleLeaveRouletteGame.sampleLeaveRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGame.sampleRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SamplePlaceBet.samplePlaceBet

class RouletteGameTest extends Specification {

    @Subject
    def rouletteGame

    def "should has player that reserved table"() {
        given:
            rouletteGame = sampleRouletteGame()
        and:
            def clientThatReservedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def tableReserved = sampleRouletteTableReserved(clientId: clientThatReservedTableId, clientTokens: clientTokens)
        when:
            rouletteGame.when(tableReserved)
        then:
            def players = rouletteGame.players()
            players.size() == 1
            def player = players.first()
            player.id().value == clientThatReservedTableId.value
            player.tokens() == clientTokens
    }

    def "should has player that joined to table"() {
        given:
            rouletteGame = sampleRouletteGame()
        and:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
        when:
            rouletteGame.when(joinedTable)
        then:
            def players = rouletteGame.players()
            players.size() == 1
            def player = players.first()
            player.id().value == clientThatJoinedTableId.value
            player.tokens() == clientTokens
    }

    def "should place bet"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            rouletteGame = sampleRouletteGame(changes: [joinedTable])
        and:
            def playerThatPlacingBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def betValue = sampleTokens(count: 5)
            def placeBet = samplePlaceBet(playerId: playerThatPlacingBetId, field: FIELD_1, value: betValue)
        when:
            rouletteGame.handle(placeBet)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.tokens() == sampleTokens(count: 55)
            def playerPlacedBets = playerThatPlacedBet.placedBets()
            playerPlacedBets.size() == 1
            def placedBet = playerPlacedBets.first()
            placedBet.value() == betValue
            placedBet.field() == FIELD_1
    }

    def "should does not have player that left game"() {
        given:
            def clientThatLeavingTableId = sampleClientId()
            def clientThatLeavingJoinedTable = sampleJoinedTable(clientId: clientThatLeavingTableId)
            def otherClientId = sampleClientId()
            def otherClientJoinedTable = sampleJoinedTable(clientId: otherClientId)
            rouletteGame = sampleRouletteGame(changes: [clientThatLeavingJoinedTable, otherClientJoinedTable])
        and:
            def playerThatLeavingGameId = sampleRoulettePlayerId(value: clientThatLeavingTableId.value)
            def leaveGame = sampleLeaveRouletteGame(playerId: playerThatLeavingGameId)
        when:
            rouletteGame.handle(leaveGame)
        then:
            def players = rouletteGame.players()
            players.size() == 1
            def player = players.first()
            player.id().value == otherClientId.value
    }

    def "should publish that player left game"() {
        given:
            def eventPublisher = new FakedRouletteGamePublisher()
            def rouletteGameLeftListener = new FakedRouletteGameLeftListener()
            eventPublisher.subscribe(rouletteGameLeftListener)
        and:
            def clientThatLeavingTableId = sampleClientId()
            def clientThatLeavingJoinedTable = sampleJoinedTable(clientId: clientThatLeavingTableId)
            rouletteGame = sampleRouletteGame(changes: [clientThatLeavingJoinedTable], eventPublisher: eventPublisher)
        and:
            def playerThatLeavingGameId = sampleRoulettePlayerId(value: clientThatLeavingTableId.value)
            def leaveGame = sampleLeaveRouletteGame(playerId: playerThatLeavingGameId)
        when:
            rouletteGame.handle(leaveGame)
        then:
            def listenedEvent = rouletteGameLeftListener.listenedEvents.first()
            with(listenedEvent) {
                gameId == rouletteGame.id()
                playerId == playerId
                playerTokens == clientThatLeavingJoinedTable.clientTokens
            }
    }

    def "should throw RoulettePlayerNotExist when player that not exists in game try to leave game"() {
        given:
            def gameId = sampleRouletteGameId()
            rouletteGame = sampleRouletteGame(id: gameId)
        and:
            def playerThatLeavingGameId = sampleRoulettePlayerId()
            def leaveGame = sampleLeaveRouletteGame(playerId: playerThatLeavingGameId)
        when:
            rouletteGame.handle(leaveGame)
        then:
            def e = thrown(RoulettePlayerNotExist)
            e.playerId == playerThatLeavingGameId
            e.gameId == gameId
    }

}

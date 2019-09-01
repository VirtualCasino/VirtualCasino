package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.roulettegame.exceptions.RoulettePlayerNotExist
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGameLeftListener
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGamePublisher
import spock.lang.Specification
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleJoinedToTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleLeaveRouletteGame.sampleLeaveRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGame.sampleRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId

class RouletteGameTest extends Specification {

    @Subject
    def rouletteGame

    def "should has player that reserved table"() {
        given:
            rouletteGame = sampleRouletteGame()
        and:
            def clientId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def tableReserved = sampleRouletteTableReserved(clientId: clientId, clientTokens: clientTokens)
        when:
            rouletteGame.when(tableReserved)
        then:
            def players = rouletteGame.players()
            players.size() == 1
            def player = players.first()
            player.id().value == clientId.value
            player.tokens() == clientTokens
    }

    def "should has player that joined to table"() {
        given:
            rouletteGame = sampleRouletteGame()
        and:
            def clientId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientId, clientTokens: clientTokens)
        when:
            rouletteGame.when(joinedTable)
        then:
            def players = rouletteGame.players()
            players.size() == 1
            def player = players.first()
            player.id().value == clientId.value
            player.tokens() == clientTokens
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
            def fakedRouletteGameLeftListener = new FakedRouletteGameLeftListener()
            eventPublisher.subscribe(fakedRouletteGameLeftListener)
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
            def listenedEvent = fakedRouletteGameLeftListener.listenedEvents.first()
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

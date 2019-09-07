package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.FakedClock
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.AnySpinNotStartedYet
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BetNotExist
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BetValueMustBePositive
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BettingTimeExceeded
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.PlacedBetsExceedPlayerFreeTokens
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.RoulettePlayerNotExist
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGameLeftListener
import pl.edu.pollub.virtualcasino.roulettegame.fakes.FakedRouletteGamePublisher
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.edu.pollub.virtualcasino.SamplePointInTime.samplePointInTime
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleJoinedTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.roulettegame.RouletteField.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleCancelRouletteBet.sampleCancelRouletteBet
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleLeaveRouletteGame.sampleLeaveRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGame.sampleRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SamplePlaceRouletteBet.samplePlaceRouletteBet
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleStartSpin.sampleStartSpin
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleRouletteBetPlaced.sampleRouletteBetPlaced
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleSpinStarted.sampleSpinStarted

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

    def "should start new spin"() {
        given:
            rouletteGame = sampleRouletteGame()
        and:
            def bettingTimeEnd = samplePointInTime()
            def startSpin = sampleStartSpin(bettingTimeEnd: bettingTimeEnd)
        when:
            rouletteGame.handle(startSpin)
        then:
            def startedSpin = rouletteGame.currentSpin$roulette_game_model()
            startedSpin != null
            startedSpin.bettingTimeEnd() == bettingTimeEnd
    }

    def "should place bet and charge player for them when betting time is not exceeded"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted], clock: new FakedClock(samplePointInTime()))
        and:
            def playerThatPlacingBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def betValue = sampleTokens(count: 5)
            def placeBet = samplePlaceRouletteBet(playerId: playerThatPlacingBetId, field: FIELD_1, value: betValue)
        when:
            rouletteGame.handle(placeBet)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.freeTokens$roulette_game_model() == sampleTokens(count: 60 - 5)
            def playerPlacedBets = playerThatPlacedBet.placedBets$roulette_game_model()
            playerPlacedBets.size() == 1
            def placedBet = playerPlacedBets.first()
            placedBet.value$roulette_game_model() == betValue
            placedBet.field$roulette_game_model() == FIELD_1
    }

    def "should throw BettingTimeExceeded when player try to place bet after betting time"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: sampleTokens(count: 60))
            def bettingTimeEnd = samplePointInTime(minute: 15)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: bettingTimeEnd)
            def clock = new FakedClock(samplePointInTime())
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted], clock: clock)
        and:
            clock.moveTo(samplePointInTime(minute: 15 + 1))
            def playerThatTryingPlaceBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def tryPlaceBet = samplePlaceRouletteBet(playerId: playerThatTryingPlaceBetId, value: sampleTokens(count: 5))
        when:
            rouletteGame.handle(tryPlaceBet)
        then:
            def e = thrown(BettingTimeExceeded)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingPlaceBetId
            e.bettingTimeEnd == bettingTimeEnd
    }

    def "should throw AnySpinNotStartedYet when player try to place bet but any spin doesn't exists"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId)
            rouletteGame = sampleRouletteGame(changes: [joinedTable])
        and:
            def playerThatTryingPlaceBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def tryPlaceBet = samplePlaceRouletteBet(playerId: playerThatTryingPlaceBetId)
        when:
            rouletteGame.handle(tryPlaceBet)
        then:
            def e = thrown(AnySpinNotStartedYet)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingPlaceBetId
    }

    @Unroll
    def "should throw BetValueMustBePositive when player try to place bet with value: #invalidBetTokensCount"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: sampleTokens(count: 60))
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted], clock: new FakedClock(samplePointInTime()))
        and:
            def playerThatTryingPlaceBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def invalidBetValue = sampleTokens(count: invalidBetTokensCount)
            def tryPlaceBet = samplePlaceRouletteBet(playerId: playerThatTryingPlaceBetId, field: FIELD_1, value: invalidBetValue)
        when:
            rouletteGame.handle(tryPlaceBet)
        then:
            def e = thrown(BetValueMustBePositive)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingPlaceBetId
            e.betValue == invalidBetValue
        where:
            invalidBetTokensCount << [0, -5]
    }

    def "should modify previous bet when player place bet for the same field"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatPlacedBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def previousBetValue = sampleTokens(count: 5)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatPlacedBetId, value: previousBetValue, field: FIELD_1)
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, betPlaced], clock: new FakedClock(samplePointInTime()))
        and:
            def nextBetValue = sampleTokens(count: 3)
            def placeNextBetForSameField = samplePlaceRouletteBet(playerId: playerThatPlacedBetId, field: FIELD_1, value: nextBetValue)
        when:
            rouletteGame.handle(placeNextBetForSameField)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.freeTokens$roulette_game_model() == sampleTokens(count: 60 - 3)
            def playerPlacedBets = playerThatPlacedBet.placedBets$roulette_game_model()
            playerPlacedBets.size() == 1
            def placedBet = playerPlacedBets.first()
            placedBet.value$roulette_game_model() == nextBetValue
            placedBet.field$roulette_game_model() == FIELD_1
    }

    def "should cancel bet"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatPlacedBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def betValue = sampleTokens(count: 5)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatPlacedBetId, value: betValue, field: FIELD_1)
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, betPlaced], clock: new FakedClock(samplePointInTime()))
        and:
            def cancelBet = sampleCancelRouletteBet(playerId: playerThatPlacedBetId, field: FIELD_1)
        when:
            rouletteGame.handle(cancelBet)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.freeTokens$roulette_game_model() == sampleTokens(count: 60)
            def playerPlacedBets = playerThatPlacedBet.placedBets$roulette_game_model()
            playerPlacedBets.size() == 0
    }

    def "should throw BettingTimeExceeded when player try to cancel bet after betting time"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: sampleTokens(count: 60))
            def bettingTimeEnd = samplePointInTime(minute: 15)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: bettingTimeEnd)
            def playerThatTryingCancelBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatTryingCancelBetId)
            def clock = new FakedClock(samplePointInTime())
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, betPlaced], clock: clock)
        and:
            clock.moveTo(samplePointInTime(minute: 15 + 1))
            def tryCancelBet = sampleCancelRouletteBet(playerId: playerThatTryingCancelBetId, field: FIELD_1)
        when:
            rouletteGame.handle(tryCancelBet)
        then:
            def e = thrown(BettingTimeExceeded)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingCancelBetId
            e.bettingTimeEnd == bettingTimeEnd
    }

    def "should throw AnySpinNotStartedYet when player try to cancel bet but any spin doesn't exists"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId)
            rouletteGame = sampleRouletteGame(changes: [joinedTable])
        and:
            def playerThatTryingPlaceBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def tryCancelBet = sampleCancelRouletteBet(playerId: playerThatTryingPlaceBetId)
        when:
            rouletteGame.handle(tryCancelBet)
        then:
            def e = thrown(AnySpinNotStartedYet)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingPlaceBetId
    }

    def "should throw BetNotExist when player try to cancel bet for field that doesn't have any bet"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted], clock: new FakedClock(samplePointInTime()))
        and:
            def playerThatTryingCancelBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def tryCancelBet = sampleCancelRouletteBet(playerId: playerThatTryingCancelBetId, field: FIELD_1)
        when:
            rouletteGame.handle(tryCancelBet)
        then:
            def e = thrown(BetNotExist)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingCancelBetId
            e.field == FIELD_1
    }

    def "should throw PlacedBetsExceedPlayerFreeTokens when player try to place bet that exceeds its tokens value"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 10)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatPlacedBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def previousBetValue = sampleTokens(count: 5)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatPlacedBetId, value: previousBetValue)
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, betPlaced], clock: new FakedClock(samplePointInTime()))
        and:
            def betThatExceedsPlayerTokens = sampleTokens(count: 6)
            def tryToPlaceBetThatExceedsPlayerTokens = samplePlaceRouletteBet(playerId: playerThatPlacedBetId, value: betThatExceedsPlayerTokens)
        when:
            rouletteGame.handle(tryToPlaceBetThatExceedsPlayerTokens)
        then:
            def e = thrown(PlacedBetsExceedPlayerFreeTokens)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatPlacedBetId
            e.betValue == betThatExceedsPlayerTokens
            e.playerFreeTokens == sampleTokens(count: 10 - 5)
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
            def playerThatTryingLeaveGameId = sampleRoulettePlayerId()
            def leaveGame = sampleLeaveRouletteGame(playerId: playerThatTryingLeaveGameId)
        when:
            rouletteGame.handle(leaveGame)
        then:
            def e = thrown(RoulettePlayerNotExist)
            e.playerId == playerThatTryingLeaveGameId
            e.gameId == gameId
    }

}

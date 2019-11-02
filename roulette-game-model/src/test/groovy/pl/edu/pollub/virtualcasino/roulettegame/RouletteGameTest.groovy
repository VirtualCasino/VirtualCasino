package pl.edu.pollub.virtualcasino.roulettegame

import pl.edu.pollub.virtualcasino.FakedClock
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BettingTimeNotEndedYet
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.SpinAlreadyFinished
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.SpinNotStartedYet
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BetNotExist
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BetValueMustBePositive
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BettingTimeEndMustBeFuture
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.BettingTimeExceeded
import pl.edu.pollub.virtualcasino.roulettegame.exceptions.CurrentSpinNotFinished
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
import static pl.edu.pollub.virtualcasino.roulettegame.ColorField.*
import static pl.edu.pollub.virtualcasino.roulettegame.ColumnField.*
import static pl.edu.pollub.virtualcasino.roulettegame.DozenFiled.*
import static pl.edu.pollub.virtualcasino.roulettegame.EvenField.*
import static pl.edu.pollub.virtualcasino.roulettegame.HalfBoardField.HALF_BOARD_1_18
import static pl.edu.pollub.virtualcasino.roulettegame.HalfDozenFiled.HALF_DOZEN_1_6
import static pl.edu.pollub.virtualcasino.roulettegame.NumberField.*
import static pl.edu.pollub.virtualcasino.roulettegame.OddField.*
import static pl.edu.pollub.virtualcasino.roulettegame.PairField.*
import static pl.edu.pollub.virtualcasino.roulettegame.QuarterFiled.*
import static pl.edu.pollub.virtualcasino.roulettegame.TripleFiled.*
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleCancelRouletteBet.sampleCancelRouletteBet
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleFinishSpin.sampleFinishSpin
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleLeaveRouletteGame.sampleLeaveRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGame.sampleRouletteGame
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SamplePlaceRouletteBet.samplePlaceRouletteBet
import static pl.edu.pollub.virtualcasino.roulettegame.samples.comands.SampleStartSpin.sampleStartSpin
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleRouletteBetPlaced.sampleRouletteBetPlaced
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleRouletteGameLeft.sampleRouletteGameLeft
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleSpinFinished.sampleSpinFinished
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
            def placeBet = samplePlaceRouletteBet(playerId: playerThatPlacingBetId, field: NUMBER_1, value: betValue)
        when:
            rouletteGame.handle(placeBet)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.tokens() == sampleTokens(count: 60)
            playerThatPlacedBet.freeTokens$roulette_game_model() == sampleTokens(count: 60 - 5)
            def playerPlacedBets = playerThatPlacedBet.placedBets$roulette_game_model()
            playerPlacedBets.size() == 1
            def placedBet = playerPlacedBets.first()
            placedBet.value$roulette_game_model() == betValue
            placedBet.field$roulette_game_model() == NUMBER_1
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
    }

    def "should throw SpinNotStartedYet when player try to place bet but any spin is not started yet"() {
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
            def e = thrown(SpinNotStartedYet)
            e.gameId == rouletteGame.id()
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
            def tryPlaceBet = samplePlaceRouletteBet(playerId: playerThatTryingPlaceBetId, field: NUMBER_1, value: invalidBetValue)
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
            def previousBetPlaced = sampleRouletteBetPlaced(playerId: playerThatPlacedBetId, betValue: previousBetValue, field: NUMBER_1)
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, previousBetPlaced], clock: new FakedClock(samplePointInTime()))
        and:
            def nextBetValue = sampleTokens(count: 3)
            def placeNextBetForSameField = samplePlaceRouletteBet(playerId: playerThatPlacedBetId, field: NUMBER_1, value: nextBetValue)
        when:
            rouletteGame.handle(placeNextBetForSameField)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.tokens() == sampleTokens(count: 60)
            playerThatPlacedBet.freeTokens$roulette_game_model() == sampleTokens(count: 60 - 3)
            def playerPlacedBets = playerThatPlacedBet.placedBets$roulette_game_model()
            playerPlacedBets.size() == 1
            def placedBet = playerPlacedBets.first()
            placedBet.value$roulette_game_model() == nextBetValue
            placedBet.field$roulette_game_model() == NUMBER_1
    }

    def "should cancel bet"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatPlacedBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def betValue = sampleTokens(count: 5)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatPlacedBetId, betValue: betValue, field: NUMBER_1)
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, betPlaced], clock: new FakedClock(samplePointInTime()))
        and:
            def cancelBet = sampleCancelRouletteBet(playerId: playerThatPlacedBetId, field: NUMBER_1)
        when:
            rouletteGame.handle(cancelBet)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.tokens() == sampleTokens(count: 60)
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
            def tryCancelBet = sampleCancelRouletteBet(playerId: playerThatTryingCancelBetId, field: NUMBER_1)
        when:
            rouletteGame.handle(tryCancelBet)
        then:
            def e = thrown(BettingTimeExceeded)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingCancelBetId
    }

    def "should throw SpinNotStartedYet when player try to cancel bet but any spin doesn't exist"() {
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
            def e = thrown(SpinNotStartedYet)
            e.gameId == rouletteGame.id()
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
            def tryCancelBet = sampleCancelRouletteBet(playerId: playerThatTryingCancelBetId, field: NUMBER_1)
        when:
            rouletteGame.handle(tryCancelBet)
        then:
            def e = thrown(BetNotExist)
            e.gameId == rouletteGame.id()
            e.playerId == playerThatTryingCancelBetId
            e.field == NUMBER_1
    }

    def "should throw PlacedBetsExceedPlayerFreeTokens when player try to place bet that exceeds its tokens value"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 10)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatPlacedBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def previousBetValue = sampleTokens(count: 5)
            def previousBetPlaced = sampleRouletteBetPlaced(playerId: playerThatPlacedBetId, betValue: previousBetValue)
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, previousBetPlaced], clock: new FakedClock(samplePointInTime()))
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

    def "should throw CurrentSpinNotFinished when try to start spin and previous spin is not finished yet"() {
        given:
            def rouletteGameId = sampleRouletteGameId()
            def previousSpinStarted = sampleSpinStarted()
            def rouletteGame = sampleRouletteGame(id: rouletteGameId, changes: [previousSpinStarted])
        and:
            def tryToStartNewSpin = sampleStartSpin()
        when:
            rouletteGame.handle(tryToStartNewSpin)
        then:
            def e = thrown(CurrentSpinNotFinished)
            e.gameId == rouletteGameId
    }

    def "should throw BettingTimeEndMustBeFuture when try to start spin with betting end time that is before current time"() {
        given:
            def rouletteGameId = sampleRouletteGameId()
            def bettingTimeEnd = samplePointInTime()
            def tryStartSpin = sampleStartSpin(bettingTimeEnd: bettingTimeEnd)
            def currentTime = samplePointInTime(minute: 1)
            rouletteGame = sampleRouletteGame(id: rouletteGameId, clock: new FakedClock(currentTime))
        when:
            rouletteGame.handle(tryStartSpin)
        then:
            def e = thrown(BettingTimeEndMustBeFuture)
            e.gameId == rouletteGameId
            e.bettingTimeEnd == bettingTimeEnd
            e.currentTime == currentTime
    }

    @Unroll
    def "should charge with #charge when player place bet on field #betField with value #betValue and field #fieldDrawn has been drawn"() {
        given:
            def clientThatJoinedTableId = sampleClientId()
            def clientTokens = sampleTokens(count: 60)
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId, clientTokens: clientTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatPlacedBetId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatPlacedBetId, betValue: sampleTokens(count: betValue), field: betField)
            def clock = new FakedClock(samplePointInTime())
            rouletteGame = sampleRouletteGame(changes: [joinedTable, spinStarted, betPlaced], clock: clock)
        and:
            clock.moveTo(samplePointInTime(minute: 2))
        and:
            def finishSpin = sampleFinishSpin(fieldDrawn: fieldDrawn)
        when:
            rouletteGame.handle(finishSpin)
        then:
            def playerThatPlacedBet = rouletteGame.players().first()
            playerThatPlacedBet.tokens() == sampleTokens(count: 60 + charge)
            playerThatPlacedBet.freeTokens$roulette_game_model() == sampleTokens(count: 60 + charge)
            def playerPlacedBets = playerThatPlacedBet.placedBets$roulette_game_model()
            playerPlacedBets.size() == 0
        where:
            betField        | fieldDrawn | betValue | charge
            NUMBER_1        | NUMBER_1   | 5        | 5 * NUMBER_1.valueMultiplier()
            NUMBER_1        | NUMBER_2   | 5        | -5
            PAIR_1_2        | NUMBER_1   | 5        | 5 * PAIR_1_2.valueMultiplier()
            PAIR_1_2        | NUMBER_3   | 5        | -5
            TRIPLE_1_2_3    | NUMBER_1   | 5        | 5 * TRIPLE_1_2_3.valueMultiplier()
            TRIPLE_1_2_3    | NUMBER_4   | 5        | -5
            QUARTER_1_2_4_5 | NUMBER_1   | 5        | 5 * QUARTER_1_2_4_5.valueMultiplier()
            QUARTER_1_2_4_5 | NUMBER_6   | 5        | -5
            HALF_DOZEN_1_6  | NUMBER_5   | 5        | 5 * HALF_DOZEN_1_6.valueMultiplier()
            HALF_DOZEN_1_6  | NUMBER_7   | 5        | -5
            DOZEN_1_12      | NUMBER_7   | 5        | 5 * DOZEN_1_12.valueMultiplier()
            DOZEN_1_12      | NUMBER_13  | 5        | -5
            COLUMN_1_34     | NUMBER_1   | 5        | 5 * COLUMN_1_34.valueMultiplier()
            COLUMN_1_34     | NUMBER_2   | 5        | -5
            HALF_BOARD_1_18 | NUMBER_1   | 5        | 5 * HALF_BOARD_1_18.valueMultiplier()
            HALF_BOARD_1_18 | NUMBER_19  | 5        | -5
            BLACK           | NUMBER_2   | 5        | 5 * BLACK.valueMultiplier()
            BLACK           | NUMBER_1   | 5        | -5
            BLACK           | NUMBER_0   | 5        | -5
            RED             | NUMBER_1   | 5        | 5 * RED.valueMultiplier()
            RED             | NUMBER_2   | 5        | -5
            RED             | NUMBER_0   | 5        | -5
            EVEN            | NUMBER_2   | 5        | 5
            EVEN            | NUMBER_1   | 5        | -5
            ODD             | NUMBER_1   | 5        | 5 * ODD.valueMultiplier()
            ODD             | NUMBER_2   | 5        | -5
    }

    def "should throw SpinNotStartedYet when try to finish spin and any spin is not started yet"() {
        given:
            rouletteGame = sampleRouletteGame()
        and:
            def tryFinishSpin = sampleFinishSpin()
        when:
            rouletteGame.handle(tryFinishSpin)
        then:
            def e = thrown(SpinNotStartedYet)
            e.gameId == rouletteGame.id()
    }

    def "should throw SpinAlreadyFinished when try to finish spin and previous spin is finished and next spin not started yet"() {
        given:
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def spinFinished = sampleSpinFinished()
            def clock = new FakedClock(samplePointInTime())
        and:
            rouletteGame = sampleRouletteGame(changes: [spinStarted, spinFinished], clock: clock)
        and:
            clock.moveTo(samplePointInTime(minute: 2))
        and:
            def tryFinishSpin = sampleFinishSpin()
        when:
            rouletteGame.handle(tryFinishSpin)
        then:
            def e = thrown(SpinAlreadyFinished)
            e.gameId == rouletteGame.id()
    }

    def "should throw BettingTimeNotEndedYet when try to finish spin and betting time is not ended yet"() {
        given:
            def bettingTimeEnd = samplePointInTime(minute: 1)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: bettingTimeEnd)
        and:
            rouletteGame = sampleRouletteGame(changes: [spinStarted], clock: new FakedClock(samplePointInTime()))
        and:
            def tryFinishSpin = sampleFinishSpin()
        when:
            rouletteGame.handle(tryFinishSpin)
        then:
            def e = thrown(BettingTimeNotEndedYet)
            e.gameId == rouletteGame.id()
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

    def "should published tokens of player that left game be equal to all player tokens when spin is started and betting time is not exceeded"() {
        given:
            def eventPublisher = new FakedRouletteGamePublisher()
            def rouletteGameLeftListener = new FakedRouletteGameLeftListener()
            eventPublisher.subscribe(rouletteGameLeftListener)
        and:
            def clientThatLeavingTableId = sampleClientId()
            def clientThatLeavingTableTokens = sampleTokens(count: 60)
            def clientThatLeavingJoinedTable = sampleJoinedTable(clientId: clientThatLeavingTableId, clientTokens: clientThatLeavingTableTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatLeavingTableId = sampleRoulettePlayerId(value: clientThatLeavingTableId.value)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatLeavingTableId, betValue: sampleTokens(count: 5))
            def clock = new FakedClock(samplePointInTime())
            rouletteGame = sampleRouletteGame(changes: [clientThatLeavingJoinedTable, spinStarted, betPlaced], clock: clock, eventPublisher: eventPublisher)
        and:
            def playerThatLeavingGameId = sampleRoulettePlayerId(value: clientThatLeavingTableId.value)
            def leaveGame = sampleLeaveRouletteGame(playerId: playerThatLeavingGameId)
        and:
            def expectedPlayerTokens = rouletteGame.players().first().tokens()
        when:
            rouletteGame.handle(leaveGame)
        then:
            def listenedEvent = rouletteGameLeftListener.listenedEvents.first()
            with(listenedEvent) {
                gameId == rouletteGame.id()
                playerId == playerId
                playerTokens == expectedPlayerTokens
            }
    }

    def "should published tokens of player that left game be equal to all player free tokens when spin is started and betting time is exceeded"() {
        given:
            def eventPublisher = new FakedRouletteGamePublisher()
            def rouletteGameLeftListener = new FakedRouletteGameLeftListener()
            eventPublisher.subscribe(rouletteGameLeftListener)
        and:
            def clientThatLeavingTableId = sampleClientId()
            def clientThatLeavingTableTokens = sampleTokens(count: 60)
            def clientThatLeavingJoinedTable = sampleJoinedTable(clientId: clientThatLeavingTableId, clientTokens: clientThatLeavingTableTokens)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime(minute: 1))
            def playerThatLeavingTableId = sampleRoulettePlayerId(value: clientThatLeavingTableId.value)
            def betPlaced = sampleRouletteBetPlaced(playerId: playerThatLeavingTableId, betValue: sampleTokens(count: 5))
            def clock = new FakedClock(samplePointInTime())
            rouletteGame = sampleRouletteGame(changes: [clientThatLeavingJoinedTable, spinStarted, betPlaced], clock: clock, eventPublisher: eventPublisher)
        and:
            clock.moveTo(samplePointInTime(minute: 1, second: 1))
        and:
            def playerThatLeavingGameId = sampleRoulettePlayerId(value: clientThatLeavingTableId.value)
            def leaveGame = sampleLeaveRouletteGame(playerId: playerThatLeavingGameId)
        and:
            def expectedPlayerTokens = rouletteGame.players().first().freeTokens$roulette_game_model()
        when:
            rouletteGame.handle(leaveGame)
        then:
            def listenedEvent = rouletteGameLeftListener.listenedEvents.first()
            with(listenedEvent) {
                gameId == rouletteGame.id()
                playerId == playerId
                playerTokens == expectedPlayerTokens
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

    def "should schedule finish of spin when spin is started"() {
        given:
            def croupier = Mock(RouletteCroupier)
            rouletteGame = sampleRouletteGame(croupier: croupier)
        and:
            def startSpin = sampleStartSpin()
        when:
            rouletteGame.handle(startSpin)
        then:
            1 * croupier.scheduleTheFinishOfSpinForGame(rouletteGame.id())
    }

    def "should schedule start of new spin when spin is finished and game has any player"() {
        given:
            def croupier = Mock(RouletteCroupier)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime())
            def joinedTable = sampleJoinedTable()
            def clock = new FakedClock(samplePointInTime())
            rouletteGame = sampleRouletteGame(croupier: croupier, changes: [spinStarted, joinedTable], clock: clock)
        and:
            clock.moveTo(samplePointInTime(minute: 1))
        and:
            def finishSpin = sampleFinishSpin()
        when:
            rouletteGame.handle(finishSpin)
        then:
            1 * croupier.scheduleTheStartOfSpinForGame(rouletteGame.id())
    }

    def "should not schedule start of new spin when spin is finished and game hasn't any player"() {
        given:
            def croupier = Mock(RouletteCroupier)
            def spinStarted = sampleSpinStarted(bettingTimeEnd: samplePointInTime())
            def playerId = sampleRoulettePlayerId()
            def joinedTable = sampleJoinedTable(clientId: sampleClientId(value: playerId.value))
            def leftTable = sampleRouletteGameLeft(playerId: playerId)
            def clock = new FakedClock(samplePointInTime())
            rouletteGame = sampleRouletteGame(croupier: croupier, changes: [spinStarted, joinedTable, leftTable], clock: clock)
        and:
            clock.moveTo(samplePointInTime(minute: 1))
        and:
            def finishSpin = sampleFinishSpin()
        when:
            rouletteGame.handle(finishSpin)
        then:
            0 * croupier.scheduleTheStartOfSpinForGame(rouletteGame.id())
    }

}

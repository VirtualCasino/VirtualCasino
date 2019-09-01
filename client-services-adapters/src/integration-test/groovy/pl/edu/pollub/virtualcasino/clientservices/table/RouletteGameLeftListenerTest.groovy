package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pollub.virtualcasino.clientservices.ClientServicesApiTest
import spock.lang.Subject

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRouletteGameId.sampleRouletteGameId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleRouletteGameLeft.sampleRouletteGameLeft

class RouletteGameLeftListenerTest extends ClientServicesApiTest {

    @Subject
    @Autowired
    RouletteGameLeftListener listener

    def "should charge client and left table"() {
        given:
            def clientThatReservedTable = setupClient()
            def reservedTableId = reserveTable(clientThatReservedTable.id())
        and:
            def clientThatJoinedTable = setupClientWithTokens(sampleTokens(count: 50))
            joinTable(reservedTableId, clientThatJoinedTable.id())
        and:
            def gameId = sampleRouletteGameId(value: reservedTableId.value)
            def playerId = sampleRoulettePlayerId(value: clientThatJoinedTable.id().value)
            def clientThatJoinedTableTokensAfterPlaying = sampleTokens(count: 100)
            def gameLeft = sampleRouletteGameLeft(gameId: gameId, playerId: playerId, playerTokens: clientThatJoinedTableTokensAfterPlaying)
        and:
            def participationOfClientThatReservedTable = sampleParticipation(clientId: clientThatReservedTable.id())
            def unexpectedParticipation = sampleParticipation(clientId: clientThatJoinedTable.id())
        when:
            listener.reactTo(gameLeft)
        then:
            def clientThatLeftGame = clientRepository.find(clientThatJoinedTable.id())
            clientThatLeftGame.tokens() == clientThatJoinedTableTokensAfterPlaying
        and:
            def reservedTable = tableRepository.find(reservedTableId)
            reservedTable != null
            def participation = reservedTable.participation()
            participation.size() == 1
            participation.contains(participationOfClientThatReservedTable)
            !participation.contains(unexpectedParticipation)
    }

}

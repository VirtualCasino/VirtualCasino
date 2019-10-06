package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotExist
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotRegistered
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.*
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedJoinedTableListener
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedPokerTableReservedListener
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedRouletteTableReservedListener
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedTableEventPublisher
import pl.edu.pollub.virtualcasino.clientservices.table.fakes.FakedTableRepository
import pl.edu.pollub.virtualcasino.clientservices.client.fakes.FakedClientRepository
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClient.*
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleTokens.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.events.SampleClientRegistered.sampleClientRegistered
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.events.SampleTokensBought.sampleTokensBought
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleJoinedTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleTableReserved.samplePokerTableReserved
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.events.SampleTableReserved.sampleRouletteTableReserved
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTable.*
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReservePokerTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReserveRouletteTable
import static pl.edu.pollub.virtualcasino.roulettegame.samples.SampleRoulettePlayerId.sampleRoulettePlayerId
import static pl.edu.pollub.virtualcasino.roulettegame.samples.events.SampleRouletteGameLeft.sampleRouletteGameLeft

class TableTest extends Specification {

    def clientRepository = new FakedClientRepository()

    @Subject
    def table

    def "should has participation of registered client that reserved table"() {
        given:
            table = sampleTable(clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId, changes: [sampleClientRegistered()]))
            def reserveTable = sampleReserveRouletteTable(clientId: clientId)
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            table.hasParticipation$client_services_model(expectedParticipation)
    }

    def "should publish that roulette table is reserved"() {
        given:
            def eventPublisher = new FakedTableEventPublisher()
            def rouletteTableReservedListener = new FakedRouletteTableReservedListener()
            eventPublisher.subscribe(rouletteTableReservedListener)
            table = sampleTable(clientRepository: clientRepository, eventPublisher: eventPublisher)
        and:
            def clientId = sampleClientId()
            def client = sampleClient(id: clientId, changes: [sampleClientRegistered()])
            clientRepository.add(client)
            def reserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            def listenedEvent = rouletteTableReservedListener.listenedEvents.first()
            with(listenedEvent) {
                tableId == table.id()
                clientId == clientId
                clientTokens == client.tokens()
                firstPlayerNick == client.nick()
            }
    }

    def "should publish that poker table is reserved"() {
        given:
            def eventPublisher = new FakedTableEventPublisher()
            def pokerTableReservedListener = new FakedPokerTableReservedListener()
            eventPublisher.subscribe(pokerTableReservedListener)
            table = sampleTable(clientRepository: clientRepository, eventPublisher: eventPublisher)
        and:
            def clientId = sampleClientId()
            def tokensBought = sampleTokensBought(tokens: sampleTokens(count: 100))
            def client = sampleClient(id: clientId, changes: [tokensBought, sampleClientRegistered()])
            clientRepository.add(client)
            def initialBidingRate = sampleTokens(count: 50)
            def reserveTable = sampleReservePokerTable(clientId: clientId, initialBidingRate: initialBidingRate)
        when:
            table.handle(reserveTable)
        then:
            def listenedEvent = pokerTableReservedListener.listenedEvents.first()
            with(listenedEvent) {
                tableId == table.id()
                clientId == clientId
                clientTokens == client.tokens()
                initialBidingRate == initialBidingRate
            }
    }

    def "should throw ClientNotRegistered when client that is not registered try to reserve table"() {
        given:
            table = sampleTable(clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def tryReserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(ClientNotRegistered)
            e.clientId == clientId
    }

    def "should throw TableAlreadyReserved when client try reserve table two times"() {
        given:
            def reservedBy = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(clientId: reservedBy)
            table = sampleTable(changes: [tableReserved], clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def tryReserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(TableAlreadyReserved)
            e.clientId == clientId
            e.tableId == table.id()
    }

    def "should throw ClientNotExist when client which not exists try to reserve table"() {
        given:
            table = sampleTable()
        and:
            def notExistingClientId = sampleClientId()
            def tryReserveTable = sampleReserveRouletteTable(clientId: notExistingClientId)
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(ClientNotExist)
            e.clientId == notExistingClientId
    }

    def "should throw ClientBusy when registered client which reserved other table try to reserve table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository, changes: [sampleClientRegistered()])
            clientRepository.add(client)
        and:
            def otherTableReserved = sampleRouletteTableReserved(clientId: clientId)
            def otherTableId = sampleTableId()
            def otherTable = sampleTable(id: otherTableId, changes: [otherTableReserved])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def tryReserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw ClientBusy when registered client which joined to other table try to reserve table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository, changes: [sampleClientRegistered()])
            clientRepository.add(client)
        and:
            def joinedToOtherTable = sampleJoinedTable(clientId: clientId)
            def otherTable = sampleTable(changes: [sampleRouletteTableReserved(), joinedToOtherTable])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def tryReserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    @Unroll
    def "should throw InitialBidingRateMustBePositive when registered client try to reserve poker table with initial biding rate equal to #invalidBidingRateValue"() {
        given:
            def tableId = sampleTableId()
            table = sampleTable(id: tableId, clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId, changes: [sampleClientRegistered()]))
        and:
            def initialBidingRate = sampleTokens(count: invalidBidingRateValue)
            def tryReserveTable = sampleReservePokerTable(clientId: clientId, initialBidingRate: initialBidingRate)
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(InitialBidingRateMustBePositive)
            e.clientId == clientId
            e.initialBidingRate == initialBidingRate
        where:
            invalidBidingRateValue << [0, -50]
    }

    def "should has participation of registered client that joined to reserved table"() {
        given:
            def tableReserved = sampleRouletteTableReserved()
            table = sampleTable(changes: [tableReserved], clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId, changes: [sampleClientRegistered()]))
            def joinToTable = sampleJoinTable(clientId: clientId)
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            table.hasParticipation$client_services_model(expectedParticipation)
    }

    def "should publish that registered client joined table"() {
        given:
            def eventPublisher = new FakedTableEventPublisher()
            def joinedTableListener = new FakedJoinedTableListener()
            eventPublisher.subscribe(joinedTableListener)
            def tableReserved = sampleRouletteTableReserved()
            table = sampleTable(changes: [tableReserved], clientRepository: clientRepository, eventPublisher: eventPublisher)
        and:
            def clientId = sampleClientId()
            def client = sampleClient(id: clientId, changes: [sampleClientRegistered()])
            clientRepository.add(client)
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def listenedEvent = joinedTableListener.listenedEvents.first()
            with(listenedEvent) {
                tableId == table.id()
                clientId == clientId
                clientTokens == client.tokens()
            }
    }

    def "should throw ClientNotRegistered when client that is not registered try to join table"() {
        given:
            def tableReserved = sampleRouletteTableReserved()
            table = sampleTable(changes: [tableReserved], clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def tryJoinTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(tryJoinTable)
        then:
            def e = thrown(ClientNotRegistered)
            e.clientId == clientId
    }

    def "should throw ClientAlreadyParticipated when client which reserved table try to join this table"() {
        given:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def tableReserved = sampleRouletteTableReserved(clientId: clientId)
            def table = sampleTable(changes: [tableReserved], clientRepository: clientRepository)
        and:
            def tryJoinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(tryJoinToTable)
        then:
            def e = thrown(ClientAlreadyParticipated)
            e.clientId == clientId
            e.tableId == table.id()
    }

    def "should throw ClientAlreadyParticipated when client try to join to table multiple times"() {
        given:
            def tableReserved = sampleRouletteTableReserved()
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def joinedToTable = sampleJoinedTable(clientId: clientId)
            table = sampleTable(changes: [tableReserved, joinedToTable], clientRepository: clientRepository)
        and:
            def tryJoinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(tryJoinToTable)
        then:
            def e = thrown(ClientAlreadyParticipated)
            e.clientId == clientId
            e.tableId == table.id()
    }

    def "should throw ClientNotExist when client which doesn't exists try join table"() {
        given:
            def tableReserved = sampleRouletteTableReserved()
            table = sampleTable(changes: [tableReserved])
        and:
            def notExistingClientId = sampleClientId()
            def tryJoinToTable = sampleJoinTable(clientId: notExistingClientId)
        when:
            table.handle(tryJoinToTable)
        then:
            def e = thrown(ClientNotExist)
            e.clientId == notExistingClientId
    }

    def "should throw ClientBusy when registered client which already reserved other table try join this table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository, changes: [sampleClientRegistered()])
            clientRepository.add(client)
        and:
            def otherTableReserved = sampleRouletteTableReserved(clientId: clientId)
            def otherTable = sampleTable(changes: [otherTableReserved])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(changes: [sampleRouletteTableReserved()], clientRepository: clientRepository)
            def tryJoinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(tryJoinToTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw ClientBusy when registered client which already joined to other table try join this table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository, changes: [sampleClientRegistered()])
            clientRepository.add(client)
        and:
            def otherTableReserved = sampleRouletteTableReserved()
            def joinedToOtherTable = sampleJoinedTable(clientId: clientId)
            def otherTable = sampleTable(changes: [otherTableReserved, joinedToOtherTable])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(changes: [sampleRouletteTableReserved()], clientRepository: clientRepository)
            def tryJoinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(tryJoinToTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw TableNotReserved when client try join table which is not reserved"() {
        given:
            table = sampleTable(clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def tryJoinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(tryJoinToTable)
        then:
            def e = thrown(TableNotReserved)
            e.clientId == clientId
            e.tableId == table.id()
    }

    @Unroll
    def "should throw TableFull when registered client try join table with game type: #gameType and max participants count: #maxParticipantCount"() {
        given:
            def tableId = sampleTableId()
            List<DomainEvent> changes = [sampleRouletteTableReserved(tableId: tableId)]
            (0..maxParticipantCount).forEach { changes.add(sampleJoinedTable()) }
            table = sampleTable(id: tableId, changes: changes, clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId, changes: [sampleClientRegistered()]))
            def tryJoinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(tryJoinToTable)
        then:
            def e = thrown(TableFull)
            e.clientId == clientId
            e.tableId == table.id()
            e.maxParticipantsCount == maxParticipantCount
        where:
            gameType   | maxParticipantCount
            "Roulette" | 10
            "Poker"    | 10
    }

    def "should throw InitialBidingRateTooHigh when registered client try reserve poker table with initial biding rate higher then their tokens count"() {
        given:
            def clientId = sampleClientId()
            def tokensBought = sampleTokensBought(tokens: sampleTokens(count: 50))
            def client = sampleClient(id: clientId, changes: [tokensBought, sampleClientRegistered()])
            clientRepository.add(client)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def tryReserveTable = sampleReservePokerTable(clientId: clientId, initialBidingRate: sampleTokens(count: 100))
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(InitialBidingRateTooHigh)
            e.clientId == clientId
            e.tokens == tokensBought.tokens
            e.initialBidingRate == tryReserveTable.initialBidingRate
    }

    def "should throw InitialBidingRateTooHigh when registered client try join poker table with initial biding rate higher then their tokens count"() {
        given:
            def clientId = sampleClientId()
            def tokensBought = sampleTokensBought(tokens: sampleTokens(count: 50))
            def client = sampleClient(id: clientId, changes: [tokensBought, sampleClientRegistered()])
            clientRepository.add(client)
        and:
            def tableId = sampleTableId()
            def tableReserved = samplePokerTableReserved(tableId: tableId, initialBidingRate: sampleTokens(count: 100))
            table = sampleTable(id: tableId, clientRepository: clientRepository, changes: [tableReserved])
        and:
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(InitialBidingRateTooHigh)
            e.clientId == clientId
            e.tokens == tokensBought.tokens
            e.initialBidingRate == tableReserved.initialBidingRate
    }

    def "should not has participation of client that reserved and left game"() {
        given:
            def clientThatReservedTableId = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(clientId: clientThatReservedTableId)
            def table = sampleTable(changes: [tableReserved])
        and:
            def playerThatLeftGameId = sampleRoulettePlayerId(value: clientThatReservedTableId.value)
            def gameLeft = sampleRouletteGameLeft(playerId: playerThatLeftGameId)
        and:
            def notExpectedParticipation = sampleParticipation(clientId: clientThatReservedTableId)
        when:
            table.when(gameLeft)
        then:
            !table.hasParticipation$client_services_model(notExpectedParticipation)
    }

    def "should not has participation of client that joined and left game"() {
        given:
            def clientThatReservedTableId = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(clientId: clientThatReservedTableId)
            def clientThatJoinedTableId = sampleClientId()
            def joinedTable = sampleJoinedTable(clientId: clientThatJoinedTableId)
            def table = sampleTable(changes: [tableReserved, joinedTable])
        and:
            def playerThatLeftGameId = sampleRoulettePlayerId(value: clientThatJoinedTableId.value)
            def gameLeft = sampleRouletteGameLeft(playerId: playerThatLeftGameId)
        and:
            def notExpectedParticipation = sampleParticipation(clientId: clientThatJoinedTableId)
        when:
            table.when(gameLeft)
        then:
            !table.hasParticipation$client_services_model(notExpectedParticipation)
    }

    def "should throw TableClosed when last client left game and other player try to reserve table"() {
        given:
            def clientId = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(clientId: clientId)
            def gameLeft = sampleRouletteGameLeft(playerId: sampleRoulettePlayerId(value: clientId.value))
            def clientRepository = new FakedClientRepository()
            def table = sampleTable(clientRepository: clientRepository, changes: [tableReserved, gameLeft])
        and:
            def otherClientId = sampleClientId()
            def otherClient = sampleClient(id: otherClientId)
            clientRepository.add(otherClient)
            def tryReserveTable = sampleReserveRouletteTable(clientId: otherClientId)
        when:
            table.handle(tryReserveTable)
        then:
            def e = thrown(TableClosed)
            e.clientId == otherClientId
            e.tableId == table.id()
    }

    def "should throw TableClosed when last client left game and other player try to join table"() {
        given:
            def clientId = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(clientId: clientId)
            def gameLeft = sampleRouletteGameLeft(playerId: sampleRoulettePlayerId(value: clientId.value))
            def clientRepository = new FakedClientRepository()
            def table = sampleTable(clientRepository: clientRepository, changes: [tableReserved, gameLeft])
        and:
            def otherClientId = sampleClientId()
            def otherClient = sampleClient(id: otherClientId)
            clientRepository.add(otherClient)
            def tryJoinTable = sampleJoinTable(clientId: otherClientId)
        when:
            table.handle(tryJoinTable)
        then:
            def e = thrown(TableClosed)
            e.clientId == otherClientId
            e.tableId == table.id()
    }

}

package pl.edu.pollub.virtualcasino.table

import pl.edu.pollub.virtualcasino.DomainEvent
import pl.edu.pollub.virtualcasino.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.client.exceptions.ClientNotExist
import pl.edu.pollub.virtualcasino.table.exceptions.*
import pl.edu.pollub.virtualcasino.table.fakes.FakedTableRepository
import pl.edu.pollub.virtualcasino.client.fakes.FakedClientRepository
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.edu.pollub.virtualcasino.client.samples.SampleClient.*
import static pl.edu.pollub.virtualcasino.client.samples.events.SampleTokensBought.sampleTokensBought
import static pl.edu.pollub.virtualcasino.table.samples.SampleTable.*
import static pl.edu.pollub.virtualcasino.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.table.samples.comands.SampleReservingTable.sampleReservePokerTable
import static pl.edu.pollub.virtualcasino.table.samples.comands.SampleReservingTable.sampleReserveRouletteTable
import static pl.edu.pollub.virtualcasino.table.samples.events.SampleJoinedToTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.table.samples.events.SampleTableReserved.samplePokerTableReserved
import static pl.edu.pollub.virtualcasino.table.samples.events.SampleTableReserved.sampleRouletteTableReserved

class TableTest extends Specification {

    def clientRepository = new FakedClientRepository()

    @Subject
    def table

    def "should has participation of client that reserved table"() {
        given:
            table = sampleTable(clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def reserveTable = sampleReserveRouletteTable(clientId: clientId)
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            table.hasParticipation(expectedParticipation)
    }

    def "should throw TableAlreadyReserved when client try reserve table two times"() {
        given:
            def reservedBy = sampleClientId()
            def tableReserved = sampleRouletteTableReserved(clientId: reservedBy)
            table = sampleTable(changes: [tableReserved], clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def reserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(TableAlreadyReserved)
            e.clientId == clientId
            e.tableId == table.id
            e.reservedBy == reservedBy
    }

    def "should throw ClientNotExist when client which not exists try to reserve table"() {
        given:
            table = sampleTable()
        and:
            def notExistingClientId = sampleClientId()
            def reserveTable = sampleReserveRouletteTable(clientId: notExistingClientId)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(ClientNotExist)
            e.clientId == notExistingClientId
    }

    def "should throw ClientBusy when client which reserved other table try to reserve table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def otherTableReserved = sampleRouletteTableReserved(clientId: clientId)
            def otherTableId = sampleTableId()
            def otherTable = sampleTable(id: otherTableId, changes: [otherTableReserved])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def reserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw ClientBusy when client which joined to other table try to reserve table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def joinedToOtherTable = sampleJoinedTable(clientId: clientId)
            def otherTable = sampleTable(changes: [sampleRouletteTableReserved(), joinedToOtherTable])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def reserveTable = sampleReserveRouletteTable(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    @Unroll
    def "should throw InitialBidingRateMustBePositive when client try to reserve poker table with initial biding rate equal to #invalidBidingRateValue"() {
        given:
            def tableId = sampleTableId()
            table = sampleTable(id: tableId, clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
        and:
            def initialBidingRate = sampleTokens(count: invalidBidingRateValue)
            def reserveTable = sampleReservePokerTable(clientId: clientId, initialBidingRate: initialBidingRate)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(InitialBidingRateMustBePositive)
            e.clientId == clientId
            e.tableId == tableId
            e.initialBidingRate == initialBidingRate
        where:
            invalidBidingRateValue << [0, -50]
    }

    def "should has participation of client that joined to reserved table"() {
        given:
            def tableReserved = sampleRouletteTableReserved()
            table = sampleTable(changes: [tableReserved], clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def joinToTable = sampleJoinTable(clientId: clientId)
        and:
            def expectedParticipation = sampleParticipation(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            table.hasParticipation(expectedParticipation)
    }

    def "should throw ClientAlreadyParticipated when client which reserved table try to joint this table"() {
        given:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def tableReserved = sampleRouletteTableReserved(clientId: clientId)
            def table = sampleTable(changes: [tableReserved], clientRepository: clientRepository)
        and:
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(ClientAlreadyParticipated)
            e.clientId == clientId
            e.tableId == table.id
    }

    def "should throw ClientAlreadyParticipated when client try to joint to table multiple times"() {
        given:
            def tableReserved = sampleRouletteTableReserved()
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def joinedToTable = sampleJoinedTable(clientId: clientId)
            table = sampleTable(changes: [tableReserved, joinedToTable], clientRepository: clientRepository)
        and:
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(ClientAlreadyParticipated)
            e.clientId == clientId
            e.tableId == table.id
    }

    def "should throw ClientNotExist when client which doesn't exists try join table"() {
        given:
            def tableReserved = sampleRouletteTableReserved()
            table = sampleTable(changes: [tableReserved])
        and:
            def notExistingClientId = sampleClientId()
            def joinToTable = sampleJoinTable(clientId: notExistingClientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(ClientNotExist)
            e.clientId == notExistingClientId
    }

    def "should throw ClientBusy when client which already reserved other table try join this table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def otherTableReserved = sampleRouletteTableReserved(clientId: clientId)
            def otherTable = sampleTable(changes: [otherTableReserved])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(changes: [sampleRouletteTableReserved()], clientRepository: clientRepository)
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw ClientBusy when client which already joined to other table try join this table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def otherTableReserved = sampleRouletteTableReserved()
            def joinedToOtherTable = sampleJoinedTable(clientId: clientId)
            def otherTable = sampleTable(changes: [otherTableReserved, joinedToOtherTable])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(changes: [sampleRouletteTableReserved()], clientRepository: clientRepository)
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
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
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(TableNotReserved)
            e.clientId == clientId
            e.tableId == table.id
    }

    @Unroll
    def "should throw TableFull when client try join table with game type: #gameType and max participants count: #maxParticipantCount"() {
        given:
            def tableId = sampleTableId()
            List<DomainEvent> changes = [sampleRouletteTableReserved(tableId: tableId)]
            (0..maxParticipantCount).forEach { changes.add(sampleJoinedTable()) }
            table = sampleTable(id: tableId, changes: changes, clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(TableFull)
            e.clientId == clientId
            e.tableId == table.id
            e.maxParticipantsCount == maxParticipantCount
        where:
            gameType   | maxParticipantCount
            "Roulette" | 10
            "Poker"    | 10
    }

    def "should throw InitialBidingRateTooHigh when client try reserve poker table with initial biding rate higher then their tokens count"() {
        given:
            def clientId = sampleClientId()
            def tokensBought = sampleTokensBought(tokens: sampleTokens(count: 50))
            def client = sampleClient(id: clientId, changes: [tokensBought])
            clientRepository.add(client)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def reserveTable = sampleReservePokerTable(clientId: clientId, initialBidingRate: sampleTokens(count: 100))
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(InitialBidingRateTooHigh)
            e.clientId == clientId
            e.tableId == table.id
            e.tokens == tokensBought.tokens
            e.initialBidingRate == reserveTable.initialBidingRate
    }

    def "should throw InitialBidingRateTooHigh when client try join poker table with initial biding rate higher then their tokens count"() {
        given:
            def clientId = sampleClientId()
            def tokensBought = sampleTokensBought(tokens: sampleTokens(count: 50))
            def client = sampleClient(id: clientId, changes: [tokensBought])
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
            e.tableId == table.id
            e.tokens == tokensBought.tokens
            e.initialBidingRate == tableReserved.initialBidingRate
    }

}

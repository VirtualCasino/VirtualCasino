package pl.edu.pollub.virtualcasino.clientservices.domain.table

import pl.edu.pollub.virtualcasino.clientservices.domain.DomainEvent
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions.ClientNotExist
import pl.edu.pollub.virtualcasino.clientservices.domain.client.fakes.FakedClientRepository
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.ClientAlreadyParticipated
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.InitialBidingRateMustBePositive
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.InitialBidingRateTooHigh
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.TableAlreadyReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.TableFull
import pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions.TableNotReserved
import pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes.FakedTableRepository
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.SampleClient.sampleTokens
import static pl.edu.pollub.virtualcasino.clientservices.domain.client.samples.events.SampleTokensCountIncreased.sampleTokensCountIncreased
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.commands.GameType.*
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.comands.SampleReserveTable.sampleReserveTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleParticipation
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.SampleTable.sampleTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.events.SampleJoinedToTable.sampleJoinedTable
import static pl.edu.pollub.virtualcasino.clientservices.domain.table.samples.events.SampleTableReserved.sampleTableReserved

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
            def reserveTable = sampleReserveTable(clientId: clientId)
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
            def tableReserved = sampleTableReserved(clientId: reservedBy)
            table = sampleTable(changes: [tableReserved])
        and:
            def clientId = sampleClientId()
            def reserveTable = sampleReserveTable(clientId: clientId)
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
            def reserveTable = sampleReserveTable(clientId: notExistingClientId)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(ClientNotExist)
            e.clientId == notExistingClientId
    }

    def "should throw ClientIsBusy when client which reserved other table try to reserve table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def otherTableReserved = sampleTableReserved(clientId: clientId)
            def otherTable = sampleTable(changes: [otherTableReserved])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def reserveTable = sampleReserveTable(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw ClientIsBusy when client which joined to other table try to reserve table"() {
        given:
            def clientId = sampleClientId()
            def tableRepository = new FakedTableRepository()
            def client = sampleClient(id: clientId, tableRepository: tableRepository)
            clientRepository.add(client)
        and:
            def joinedToOtherTable = sampleJoinedTable(clientId: clientId)
            def otherTable = sampleTable(changes: [sampleTableReserved(), joinedToOtherTable])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def reserveTable = sampleReserveTable(clientId: clientId)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    @Unroll
    def "should throw InitialBidingRateMustBePositive when client try to reserve poker table with initial biding rate equal to #bidingRate"() {
        given:
            def tableId = sampleTableId()
            table = sampleTable(id: tableId, clientRepository: clientRepository)
        and:
            def clientId = sampleClientId()
            clientRepository.add(sampleClient(id: clientId))
        and:
            def initialBidingRate = sampleTokens(count: bidingRate)
            def reserveTable = sampleReserveTable(clientId: clientId, gameType: POKER, initialBidingRate: initialBidingRate)
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(InitialBidingRateMustBePositive)
            e.clientId == clientId
            e.tableId == tableId
            e.initialBidingRate == initialBidingRate
        where:
            bidingRate << [0, -50]
    }

    def "should has participation of client that joined to reserved table"() {
        given:
            def tableReserved = sampleTableReserved()
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
            def tableReserved = sampleTableReserved(clientId: clientId)
            def table = sampleTable(changes: [tableReserved])
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
            def tableReserved = sampleTableReserved()
            def clientId = sampleClientId()
            def joinedToTable = sampleJoinedTable(clientId: clientId)
            table = sampleTable(changes: [tableReserved, joinedToTable])
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
            def tableReserved = sampleTableReserved()
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
            def otherTableReserved = sampleTableReserved(clientId: clientId)
            def otherTable = sampleTable(changes: [otherTableReserved])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(changes: [sampleTableReserved()], clientRepository: clientRepository)
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
            def otherTableReserved = sampleTableReserved()
            def joinedToOtherTable = sampleJoinedTable(clientId: clientId)
            def otherTable = sampleTable(changes: [otherTableReserved, joinedToOtherTable])
            tableRepository.add(otherTable)
        and:
            table = sampleTable(changes: [sampleTableReserved()], clientRepository: clientRepository)
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(ClientBusy)
            e.clientId == clientId
    }

    def "should throw TableNotReserved when client try join table which is not reserved"() {
        given:
            table = sampleTable()
        and:
            def clientId = sampleClientId()
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
            List<DomainEvent> changes = [sampleTableReserved(tableId: tableId, gameType: gameType)]
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
            gameType | maxParticipantCount
            ROULETTE | 10
            POKER    | 10
    }

    def "should throw InitialBidingRateTooHigh when client try reserve poker table with initial biding rate higher then their tokens count"() {
        given:
            def clientId = sampleClientId()
            def tokensCountIncreased = sampleTokensCountIncreased(tokens: sampleTokens(count: 50))
            def client = sampleClient(id: clientId, changes: [tokensCountIncreased])
            clientRepository.add(client)
        and:
            table = sampleTable(clientRepository: clientRepository)
            def reserveTable = sampleReserveTable(clientId: clientId, gameType: POKER, initialBidingRate: sampleTokens(count: 100))
        when:
            table.handle(reserveTable)
        then:
            def e = thrown(InitialBidingRateTooHigh)
            e.clientId == clientId
            e.tableId == table.id
            e.tokens == tokensCountIncreased.tokens
            e.initialBidingRate == reserveTable.initialBidingRate
    }

    def "should throw InitialBidingRateTooHigh when client try join poker table with initial biding rate higher then their tokens count"() {
        given:
            def clientId = sampleClientId()
            def tokensCountIncreased = sampleTokensCountIncreased(tokens: sampleTokens(count: 50))
            def client = sampleClient(id: clientId, changes: [tokensCountIncreased])
            clientRepository.add(client)
        and:
            def tableId = sampleTableId()
            def tableReserved = sampleTableReserved(tableId: tableId, gameType: POKER, initialBidingRate: sampleTokens(count: 100))
            table = sampleTable(id: tableId, clientRepository: clientRepository, changes: [tableReserved])
        and:
            def joinToTable = sampleJoinTable(clientId: clientId)
        when:
            table.handle(joinToTable)
        then:
            def e = thrown(InitialBidingRateTooHigh)
            e.clientId == clientId
            e.tableId == table.id
            e.tokens == tokensCountIncreased.tokens
            e.initialBidingRate == tableReserved.initialBidingRate
    }

}

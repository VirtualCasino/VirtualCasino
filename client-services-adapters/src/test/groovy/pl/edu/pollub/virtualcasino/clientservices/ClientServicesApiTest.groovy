package pl.edu.pollub.virtualcasino.clientservices

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pollub.virtualcasino.ClientServicesBoundedContextBaseIntegrationTest
import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.client.Nick
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.comands.SampleRegisterClient.sampleRegisterClient
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.SampleNick.sampleNick
import static pl.edu.pollub.virtualcasino.clientservices.samples.client.samples.events.SampleTokensBought.sampleTokensBought
import static pl.edu.pollub.virtualcasino.clientservices.samples.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReservePokerTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReserveRouletteTable

class ClientServicesApiTest extends ClientServicesBoundedContextBaseIntegrationTest {

    @Autowired
    TableRepository tableRepository

    @Autowired
    ClientRepository clientRepository

    def setup() {
        clientRepository.clear()
        tableRepository.clear()
    }

    TableId tableUriToId(URI tableUri) {
        def tableUriSegments = tableUri.toString().split("/")
        def tableIdValue = tableUriSegments[tableUriSegments.length - 1]
        return sampleTableId(value: UUID.fromString(tableIdValue))
    }

    ClientId clientUriToId(URI clientUri) {
        def clientUriSegments = clientUri.toString().split("/")
        def clientIdValue = clientUriSegments[clientUriSegments.length - 1]
        return sampleClientId(value: UUID.fromString(clientIdValue))
    }

    Client setupClient() {
        def clientId = registerClient(sampleNick())
        return clientRepository.find(clientId)
    }

    Client setupClientWithTokens(Tokens tokens) {
        def clientId = registerClient(sampleNick())
        http.put(URI.create("/virtual-casino/casino-services/clients/tokens"), sampleTokensBought(clientId: clientId, tokens: tokens))
        return clientRepository.find(clientId)
    }

    ClientId registerClient(Nick nick) {
        def clientUri = http.postForLocation(URI.create("/virtual-casino/casino-services/clients"), sampleRegisterClient(nick: nick))
        return clientUriToId(clientUri)
    }

    TableId reserveTable(ClientId clientThatReservedTableId) {
        def tableUri = http.postForLocation(URI.create("/virtual-casino/casino-services/tables/roulette"), sampleReserveRouletteTable(clientId: clientThatReservedTableId))
        return tableUriToId(tableUri)
    }

    TableId reservePokerTable(ClientId clientThatReservedTableId, Tokens initialBidingRate) {
        def reserveTable = sampleReservePokerTable(clientId: clientThatReservedTableId, initialBidingRate: initialBidingRate)
        def tableUri = http.postForLocation(URI.create("/virtual-casino/casino-services/tables/poker"), reserveTable)
        return tableUriToId(tableUri)
    }

    TableId joinTable(TableId tableId, ClientId clientThatJoinedToTableId) {
        def tableUri = http.postForLocation(URI.create("/virtual-casino/casino-services/tables/participation"), sampleJoinTable(tableId: tableId, clientId: clientThatJoinedToTableId))
        return tableUriToId(tableUri)
    }
}

package pl.edu.pollub.virtualcasino.clientservices

import org.springframework.beans.factory.annotation.Autowired
import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.client.ClientRepository
import pl.edu.pollub.virtualcasino.clientservices.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository

import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClient.sampleClient
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.SampleClientId.sampleClientId
import static pl.edu.pollub.virtualcasino.clientservices.client.samples.events.SampleTokensBought.sampleTokensBought
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.SampleTableId.sampleTableId
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleJoinTable.sampleJoinTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReservePokerTable
import static pl.edu.pollub.virtualcasino.clientservices.table.samples.comands.SampleReserveTable.sampleReserveRouletteTable

class ClientServicesApiTest extends BaseIntegrationTest {

    @Autowired
    TableRepository tableRepository

    @Autowired
    ClientRepository clientRepository

    def cleanup() {
        clientRepository.clear()
        tableRepository.clear()
    }

    TableId uriToId(URI tableUri) {
        def tableUriSegments = tableUri.toString().split("/")
        def tableIdValue = tableUriSegments[tableUriSegments.length - 1]
        return sampleTableId(value: UUID.fromString(tableIdValue))
    }

    Client setupClient() {
        def clientId = sampleClientId()
        def client = sampleClient(id: clientId)
        clientRepository.add(client)
        return client
    }

    Client setupClientWithTokens(Tokens tokens) {
        def clientId = sampleClientId()
        def client = sampleClient(id: clientId)
        clientRepository.add(client)
        http.put(URI.create("/casino-services/clients/tokens"), sampleTokensBought(clientId: clientId, tokens: tokens))
        return client
    }

    TableId reserveTable(ClientId clientThatReservedTableId) {
        def tableUri = http.postForLocation(URI.create("/casino-services/tables/roulette"), sampleReserveRouletteTable(clientId: clientThatReservedTableId))
        return uriToId(tableUri)
    }

    TableId reservePokerTable(ClientId clientThatReservedTableId, Tokens initialBidingRate) {
        def reserveTable = sampleReservePokerTable(clientId: clientThatReservedTableId, initialBidingRate: initialBidingRate)
        def tableUri = http.postForLocation(URI.create("/casino-services/tables/poker"), reserveTable)
        return uriToId(tableUri)
    }

    TableId joinTable(TableId tableId, ClientId clientThatJoinedToTableId) {
        def tableUri = http.postForLocation(URI.create("/casino-services/tables/participation"), sampleJoinTable(tableId: tableId, clientId: clientThatJoinedToTableId))
        return uriToId(tableUri)
    }
}

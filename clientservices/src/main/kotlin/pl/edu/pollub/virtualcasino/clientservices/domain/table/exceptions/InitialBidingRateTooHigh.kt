package pl.edu.pollub.virtualcasino.clientservices.domain.table.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Tokens
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import java.lang.IllegalStateException

class InitialBidingRateTooHigh(val clientId: ClientId, val tableId: TableId, val tokens: Tokens, val initialBidingRate: Tokens)
    : IllegalStateException("Client with id: ${clientId.value} can't join table with id: ${tableId.value} because initial biding rate: ${initialBidingRate.count} is higher than their tokens count: ${tokens.count}")
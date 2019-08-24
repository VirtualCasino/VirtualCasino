package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableAlreadyReserved

internal class ReservationSpecification {

    internal fun canReserve(table: Table, client: Client) {
        val clientId = client.id()
        if (table.isReserved()) throw TableAlreadyReserved(clientId, table.id())
        if (client.doesParticipateToAnyTable()) throw ClientBusy(clientId)
    }

}
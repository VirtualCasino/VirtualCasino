package pl.edu.pollub.virtualcasino.clientservices.table

import pl.edu.pollub.virtualcasino.clientservices.client.Client
import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientBusy
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.ClientAlreadyParticipated
import pl.edu.pollub.virtualcasino.clientservices.table.exceptions.TableNotReserved

internal class JoiningSpecification {

    internal fun canJoin(table: Table, client: Client) {
        val clientId = client.id()
        if (!table.isReserved()) throw TableNotReserved(clientId, table.id())
        if (table.hasParticipation(Participation(clientId))) throw ClientAlreadyParticipated(clientId, table.id())
        if (client.doesParticipateToAnyTable()) throw ClientBusy(clientId)
        table.requirements().check(client)
    }

}
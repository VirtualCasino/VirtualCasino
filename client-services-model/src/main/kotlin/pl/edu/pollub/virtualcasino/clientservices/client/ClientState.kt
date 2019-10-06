package pl.edu.pollub.virtualcasino.clientservices.client

import pl.edu.pollub.virtualcasino.clientservices.client.exceptions.ClientNotRegistered

internal interface ClientState {

    fun canBuyTokens(client: Client): Boolean

    fun canReserveTable(client: Client): Boolean

    fun canJoinTable(client: Client): Boolean

    fun nick(): Nick?

}

internal class NotRegistered: ClientState {

    override fun canBuyTokens(client: Client): Boolean = throw ClientNotRegistered(client.id())

    override fun canReserveTable(client: Client): Boolean = throw ClientNotRegistered(client.id())

    override fun canJoinTable(client: Client): Boolean = throw ClientNotRegistered(client.id())

    override fun nick(): Nick? = null

}

internal class Registered(private val nick: Nick): ClientState {

    override fun canBuyTokens(client: Client): Boolean = true

    override fun canReserveTable(client: Client): Boolean = true

    override fun canJoinTable(client: Client): Boolean = true

    override fun nick(): Nick = nick

}
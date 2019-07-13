package pl.edu.pollub.virtualcasino.clientservices.domain.client.commands

import java.util.*

data class CreateClient(val id: CreateClientId = CreateClientId())

data class CreateClientId(val value: String = UUID.randomUUID().toString())
package pl.edu.pollub.virtualcasino.clientservices.domain.client.exceptions

import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import java.lang.IllegalStateException

class ClientNotExist(val clientId: ClientId): IllegalStateException("Client with id: ${clientId.value} doesn't exist")
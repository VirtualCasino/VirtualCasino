package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("client_view")
class ClientView(@Id val id: UUID = UUID.randomUUID(),
                 val clientViewId: UUID,
                 var tokensCount: Int)
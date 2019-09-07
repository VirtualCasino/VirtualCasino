package pl.edu.pollub.virtualcasino.clientservices.table.projection

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import pl.edu.pollub.virtualcasino.clientservices.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import java.util.*
import java.util.UUID.*

@Document("table_participants")
data class TableParticipants(
        @Id val id: UUID = randomUUID(),
        val tableId: TableId,
        var participation: Set<Participation> = emptySet()
)
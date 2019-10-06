package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document("table_view")
class TableView(@Id val id: UUID = UUID.randomUUID(),
                val tableViewId: UUID,
                val firstPlayerNick: String,
                val playersIds: MutableSet<String>,
                val maxPlayersCount: Int,
                val gameType: String)
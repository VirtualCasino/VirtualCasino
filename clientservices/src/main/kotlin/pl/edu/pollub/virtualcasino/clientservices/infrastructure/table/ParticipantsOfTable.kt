package pl.edu.pollub.virtualcasino.clientservices.infrastructure.table

import pl.edu.pollub.virtualcasino.clientservices.domain.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId

data class ParticipantsOfTable(val tableId: TableId, val participation: List<Participation>)
package pl.edu.pollub.virtualcasino.clientservices.domain.table.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.clientservices.domain.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.domain.table.Table
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.domain.table.TableRepository

class FakedTableRepository implements TableRepository {

    private List<Table> aggregates = []

    @Override
    boolean add(@NotNull Table aggregate) {
        return aggregates.add(aggregate)
    }

    @Override
    Table find(@NotNull TableId id) {
        return aggregates.find { it.id == id }
    }

    @Override
    void clear() {
        aggregates.clear()
    }

    @Override
    boolean containsWithParticipation(@NotNull Participation participation) {
        return aggregates.any { it.hasParticipation(participation) }
    }

    List<Table> findAll() {
        return aggregates
    }
}

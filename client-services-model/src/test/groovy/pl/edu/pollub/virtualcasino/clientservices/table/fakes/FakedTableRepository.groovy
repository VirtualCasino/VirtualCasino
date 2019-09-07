package pl.edu.pollub.virtualcasino.clientservices.table.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.clientservices.table.Participation
import pl.edu.pollub.virtualcasino.clientservices.table.Table
import pl.edu.pollub.virtualcasino.clientservices.table.TableId
import pl.edu.pollub.virtualcasino.clientservices.table.TableRepository

class FakedTableRepository implements TableRepository {

    private List<Table> aggregates = []

    @Override
    boolean add(@NotNull Table aggregate) {
        return aggregates.add(aggregate)
    }

    @Override
    Table find(@NotNull TableId id) {
        return aggregates.find { it.id() == id }
    }

    @Override
    void clear() {
        aggregates.clear()
    }

    @Override
    boolean containsWithParticipation(@NotNull Participation participation) {
        return aggregates.any { it.hasParticipation$client_services_model(participation) }
    }

    List<Table> findAll() {
        return aggregates
    }
}

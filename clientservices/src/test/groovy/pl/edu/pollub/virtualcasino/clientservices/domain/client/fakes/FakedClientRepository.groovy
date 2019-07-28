package pl.edu.pollub.virtualcasino.clientservices.domain.client.fakes

import org.jetbrains.annotations.NotNull
import pl.edu.pollub.virtualcasino.clientservices.domain.client.Client
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientId
import pl.edu.pollub.virtualcasino.clientservices.domain.client.ClientRepository

class FakedClientRepository implements ClientRepository {

    List<Client> aggregates = []

    @Override
    Client find(ClientId id) {
        return aggregates.find { it.id == id }
    }

    @Override
    boolean add(@NotNull Client aggregate) {
        return aggregates.add(aggregate)
    }

    @Override
    void clear() {
        aggregates.clear()
    }
}

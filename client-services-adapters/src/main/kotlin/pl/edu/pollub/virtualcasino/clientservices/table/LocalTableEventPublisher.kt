package pl.edu.pollub.virtualcasino.clientservices.table

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.eventpublisher.LocalEventPublisher

@Component
class LocalTableEventPublisher: LocalEventPublisher(), TableEventPublisher
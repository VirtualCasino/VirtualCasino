package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.stereotype.Component
import pl.edu.pollub.virtualcasino.eventpublisher.LocalEventPublisher

@Component
class LocalClientEventPublisher: LocalEventPublisher(), ClientEventPublisher
package pl.edu.pollub.virtualcasino.clientservices.client

import org.springframework.context.annotation.Configuration
import javax.annotation.PostConstruct

@Configuration
class ClientEventsSubscription(
        private val publisher: ClientEventPublisher,
        private val clientBoughtTokensListener: ViewClientBoughtTokensListener,
        private val clientRegisteredListener: ViewClientRegisteredListener
) {

    @PostConstruct
    fun subscribeListeners() {
        publisher.subscribe(clientBoughtTokensListener)
        publisher.subscribe(clientRegisteredListener)
    }

}
package pl.edu.pollub.virtualcasino

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification
import spock.util.concurrent.PollingConditions

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [ClientServicesBoundedContext.class])
@ActiveProfiles("integration-test")
class ClientServicesBoundedContextBaseIntegrationTest extends Specification {

    @Autowired
    TestRestTemplate http

    @Autowired
    MongoTemplate mongo

    PollingConditions conditions

    def setup() {
        conditions = new PollingConditions(timeout: 12, initialDelay: 0, factor: 1)
    }
}

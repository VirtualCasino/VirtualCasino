package pl.edu.pollub.virtualcasino

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [ClientServicesAdaptersConfig.class])
@ActiveProfiles("integration-test")
class BaseIntegrationTest extends Specification {

    @Autowired
    TestRestTemplate http

    @Autowired
    MongoTemplate mongo

}

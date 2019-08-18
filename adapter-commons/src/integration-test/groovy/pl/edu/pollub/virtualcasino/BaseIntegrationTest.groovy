package pl.edu.pollub.virtualcasino

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@SpringBootTest(classes = [AdapterCommonsConfig.class])
@ActiveProfiles("integration-test")
class BaseIntegrationTest extends Specification {

    @Autowired
    MongoTemplate mongo

}

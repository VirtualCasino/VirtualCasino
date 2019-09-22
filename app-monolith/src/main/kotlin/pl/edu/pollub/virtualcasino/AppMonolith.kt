package pl.edu.pollub.virtualcasino

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement(proxyTargetClass = true)
class AppMonolith {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(AppMonolith::class.java, *args)
        }
    }

}
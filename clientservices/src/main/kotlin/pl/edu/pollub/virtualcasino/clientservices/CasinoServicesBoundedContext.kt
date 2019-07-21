package pl.edu.pollub.virtualcasino.clientservices

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
open class CasinoServicesBoundedContext {

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(CasinoServicesBoundedContext::class.java, *args)
        }
    }

}

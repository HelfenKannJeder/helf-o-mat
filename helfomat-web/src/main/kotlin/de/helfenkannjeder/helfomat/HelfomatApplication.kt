package de.helfenkannjeder.helfomat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
open class HelfomatApplication

fun main(args: Array<String>) {
    runApplication<HelfomatApplication>(*args)
}
package de.helfenkannjeder.helfomat

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry
import kotlin.system.exitProcess

/**
 * @author Valentin Zickner
 */
@SpringBootApplication
@EnableBatchProcessing
@EnableRetry
open class ImportingApplication

fun main(args: Array<String>) {
    val context = runApplication<ImportingApplication>(*args)
    exitProcess(SpringApplication.exit(context))
}

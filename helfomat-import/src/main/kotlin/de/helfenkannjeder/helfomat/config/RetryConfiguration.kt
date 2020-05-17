package de.helfenkannjeder.helfomat.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.RetryCallback

import org.springframework.retry.RetryContext
import org.springframework.retry.RetryListener

import org.springframework.retry.listener.RetryListenerSupport
import java.util.*

@Configuration
open class RetryConfiguration {

    @Bean
    open fun retryListeners(): List<RetryListener?>? {
        val log: Logger = LoggerFactory.getLogger(javaClass)
        return Collections.singletonList(object : RetryListenerSupport() {
            override fun <T, E : Throwable?> onError(
                context: RetryContext, callback: RetryCallback<T, E>, throwable: Throwable) {
                log.warn("Retryable method {} threw {}th exception {}",
                    context.getAttribute("context.name"),
                    context.retryCount, throwable.toString())
            }
        })
    }

}
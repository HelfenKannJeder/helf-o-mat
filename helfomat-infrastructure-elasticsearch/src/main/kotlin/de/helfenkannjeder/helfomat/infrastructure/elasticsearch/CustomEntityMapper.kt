package de.helfenkannjeder.helfomat.infrastructure.elasticsearch

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.data.elasticsearch.core.EntityMapper
import org.springframework.data.mapping.MappingException
import java.io.IOException
import java.util.*

/**
 * based on spring data elasticsearch DefaultEntityMapper
 */
internal class CustomEntityMapper : EntityMapper {
    private val objectMapper: ObjectMapper = ObjectMapper()

    init {
        objectMapper.registerModule(KotlinModule())
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(Jdk8Module())
    }

    override fun mapToString(`object`: Any): String {
        return objectMapper.writeValueAsString(`object`)
    }

    override fun mapObject(source: Any): Map<String, Any> {
        return try {
            objectMapper.readValue<HashMap<String, Any>>(mapToString(source))
        } catch (e: IOException) {
            throw MappingException(e.message, e)
        }
    }

    override fun <T> mapToObject(source: String, clazz: Class<T>): T {
        return objectMapper.readValue(source, clazz)
    }

    override fun <T> readObject(source: Map<String, Any>, targetType: Class<T>): T {
        return try {
            mapToObject(mapToString(source), targetType)
        } catch (var4: IOException) {
            throw MappingException(var4.message, var4)
        }
    }
}
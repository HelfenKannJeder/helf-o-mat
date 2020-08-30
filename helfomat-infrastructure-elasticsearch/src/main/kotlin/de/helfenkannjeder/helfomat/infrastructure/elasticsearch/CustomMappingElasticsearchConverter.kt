package de.helfenkannjeder.helfomat.infrastructure.elasticsearch

import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty
import org.springframework.data.mapping.context.MappingContext
import org.springframework.data.util.TypeInformation
import java.time.LocalTime

open class CustomMappingElasticsearchConverter(
    mappingContext: MappingContext<out ElasticsearchPersistentEntity<*>, ElasticsearchPersistentProperty>
) : MappingElasticsearchConverter(mappingContext) {

    override fun <R : Any?> readValue(source: Any?, property: ElasticsearchPersistentProperty, targetType: TypeInformation<R>): R? {
        val rawType = targetType.type
        if (rawType == LocalTime::class.java && source is List<*> && source.size == 2 && source[0] is Int && source[1] is Int) {
            val hour = source[0] as Int
            val minute = source[1] as Int
            return LocalTime.of(hour, minute) as R
        }
        return super.readValue(source, property, targetType)
    }

    override fun getWriteSimpleValue(value: Any): Any? {
        if (value is LocalTime) {
            return intArrayOf(value.hour, value.minute)
        }
        return super.getWriteSimpleValue(value)
    }
}
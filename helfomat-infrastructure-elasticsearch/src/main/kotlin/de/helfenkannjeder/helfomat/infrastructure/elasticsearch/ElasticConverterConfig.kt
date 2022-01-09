package de.helfenkannjeder.helfomat.infrastructure.elasticsearch

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import org.springframework.data.elasticsearch.client.ClientConfiguration
import org.springframework.data.elasticsearch.client.RestClients
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions
import java.time.DayOfWeek
import java.time.LocalTime

@Configuration
@Profile("!" + ProfileRegistry.TEST)
open class ElasticConverterConfig(
    val elasticsearchConfiguration: ElasticsearchConfiguration
) : AbstractElasticsearchConfiguration() {

    override fun elasticsearchClient(): RestHighLevelClient =
        RestClients.create(ClientConfiguration.create(elasticsearchConfiguration.uri)).rest()

    @Bean
    override fun elasticsearchCustomConversions(): ElasticsearchCustomConversions? =
        ElasticsearchCustomConversions(
            listOf(
                ListToLocalTimeConverter(),
                LocalTimeToListConverter(),
                StringToDayOfWeekConverter(),
                DayOfWeekToStringConverter()
            )
        )

}

@ReadingConverter
class StringToDayOfWeekConverter : Converter<String, DayOfWeek> {
    override fun convert(source: String): DayOfWeek = DayOfWeek.valueOf(source)
}

@WritingConverter
class DayOfWeekToStringConverter : Converter<DayOfWeek, String> {
    override fun convert(source: DayOfWeek): String = source.toString()
}

@ReadingConverter
class ListToLocalTimeConverter : Converter<List<Any>, LocalTime> {
    override fun convert(source: List<Any>): LocalTime? {
        if (source.size == 2 && source[0] is Int && source[1] is Int) {
            val hour = source[0] as Int
            val minute = source[1] as Int
            return LocalTime.of(hour, minute)
        }
        return null
    }
}

@WritingConverter
class LocalTimeToListConverter : Converter<LocalTime, List<Any>> {
    override fun convert(source: LocalTime): List<Any> = listOf(source.hour, source.minute)
}
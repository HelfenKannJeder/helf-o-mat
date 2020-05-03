package de.helfenkannjeder.helfomat.importing

import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationReader
import de.helfenkannjeder.helfomat.core.organization.OrganizationRepository
import de.helfenkannjeder.helfomat.core.organization.event.OrganizationEvent
import de.helfenkannjeder.helfomat.infrastructure.batch.listener.UniqueOrganizationUrlNameOrganizationProcessor
import de.helfenkannjeder.helfomat.infrastructure.batch.processor.AnswerQuestionsProcessor
import de.helfenkannjeder.helfomat.infrastructure.batch.processor.OrganizationDifferenceProcessor
import de.helfenkannjeder.helfomat.infrastructure.batch.writer.OrganizationItemWriter
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.ElasticsearchConfiguration
import de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization.ElasticsearchOrganizationRepository
import de.helfenkannjeder.helfomat.rest.RestOrganizationEventPublisher
import org.springframework.batch.core.ExitStatus
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepExecution
import org.springframework.batch.core.StepExecutionListener
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.util.StreamUtils
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.function.Function

/**
 * @author Valentin Zickner
 */
@Configuration
@EnableConfigurationProperties(ElasticsearchConfiguration::class)
open class ImportBatchConfig {

    @Bean
    @Qualifier("importSteps")
    open fun importSteps(stepBuilderFactory: StepBuilderFactory,
                         organizationReaders: List<OrganizationReader>,
                         answerQuestionsProcessor: AnswerQuestionsProcessor,
                         elasticsearchConfiguration: ElasticsearchConfiguration,
                         elasticsearchTemplate: ElasticsearchTemplate,
                         restOrganizationEventPublisher: RestOrganizationEventPublisher,
                         organizationRepository: OrganizationRepository,
                         @Value("classpath:/mapping/organization.json") organizationMapping: Resource,
                         @Qualifier("legacyTransactionManager") transactionManager: PlatformTransactionManager): List<Step> {
        return organizationReaders
            .map {
                val uniqueOrganizationUrlNameOrganizationProcessor = UniqueOrganizationUrlNameOrganizationProcessor()
                val indexName = elasticsearchConfiguration.index + "-" + it.name
                val elasticsearchOrganizationRepository = ElasticsearchOrganizationRepository(elasticsearchConfiguration, elasticsearchTemplate, indexName)
                val organizationDifferenceProcessor = OrganizationDifferenceProcessor(organizationRepository, organizationRepository)
                stepBuilderFactory["import" + it.javaClass.simpleName]
                    .chunk<Organization, Pair<Organization, List<OrganizationEvent>>>(5)
                    .reader { it.read() }
                    .processor(Function {
                        organizationDifferenceProcessor.process(
                            answerQuestionsProcessor.process(
                                uniqueOrganizationUrlNameOrganizationProcessor.apply(it)
                            )
                        )
                    })
                    .writer { organizationInfo: List<Pair<Organization, List<OrganizationEvent>>> ->
                        val organizationItemWriter = OrganizationItemWriter(organizationRepository)
                        organizationInfo.forEach {
                            restOrganizationEventPublisher.publishEvents(it.first, it.second)
                            organizationItemWriter.write(listOf(it.first))
                        }
                    }
                    .listener(OrganizationStepExecutionListener(elasticsearchOrganizationRepository, organizationMapping))
                    .listener(uniqueOrganizationUrlNameOrganizationProcessor)
                    .transactionManager(transactionManager)
                    .build()
            }
    }

    private class OrganizationStepExecutionListener internal constructor(
        private val organizationRepository: ElasticsearchOrganizationRepository,
        private val organizationMapping: Resource
    ) : StepExecutionListener {

        override fun beforeStep(stepExecution: StepExecution) {

            try {
                val mapping = StreamUtils.copyToString(organizationMapping.inputStream, StandardCharsets.UTF_8)
                organizationRepository.createIndex(mapping)
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }

        override fun afterStep(stepExecution: StepExecution): ExitStatus {
            return stepExecution.exitStatus
        }

    }
}
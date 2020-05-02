package de.helfenkannjeder.helfomat.infrastructure.typo3

import de.helfenkannjeder.helfomat.core.ProfileRegistry
import de.helfenkannjeder.helfomat.core.organization.Organization
import de.helfenkannjeder.helfomat.core.organization.OrganizationReader
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

/**
 * @author Valentin Zickner
 */
@Component
@Order(100)
@Transactional(propagation = Propagation.REQUIRED, transactionManager = "legacyTransactionManager")
@Profile("!" + ProfileRegistry.DISABLE_TYPO3_IMPORT)
open class Typo3OrganizationReader @Autowired constructor(private val typo3OrganizationRepository: Typo3OrganizationRepository, private val typo3OrganizationProcessor: Typo3OrganizationProcessor) : OrganizationReader {

    @Volatile
    private var results: List<Organization>? = null

    @Volatile
    private var page = 0

    @Volatile
    private var current = 0

    override val name = "typo3"

    override fun read(): Organization? {
        if (results == null || current >= results?.size ?: 0) {
            LOGGER.debug("Reading page $page")
            results = doReadPage()
            page++
            current = 0
        }
        val next = current++

        return if (next < (results?.size ?: 0)) {
            results?.get(next)
        } else {
            null
        }
    }

    private fun doReadPage(): List<Organization> {
        return typo3OrganizationRepository
            .findAvailable(PageRequest.of(page, PAGE_SIZE))
            .map { typo3OrganizationProcessor.process(it) }
            .filterNotNull()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Typo3OrganizationReader::class.java)
        private const val PAGE_SIZE = 10
    }

}
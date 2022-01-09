package de.helfenkannjeder.helfomat.infrastructure.elasticsearch.organization

import de.helfenkannjeder.helfomat.core.geopoint.BoundingBox
import de.helfenkannjeder.helfomat.core.geopoint.GeoPoint
import de.helfenkannjeder.helfomat.core.organization.*
import org.apache.lucene.search.join.ScoreMode
import org.elasticsearch.common.unit.DistanceUnit
import org.elasticsearch.index.IndexNotFoundException
import org.elasticsearch.index.query.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.SearchHitsIterator
import org.springframework.data.elasticsearch.core.document.Document
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery
import org.springframework.data.elasticsearch.core.query.Query
import java.util.*

class ElasticsearchOrganizationRepository(
    private val elasticsearchOperations: ElasticsearchOperations,
    private val indexName: String
) : OrganizationRepository {

    override fun findOrganizationWithSameTypeInDistance(defaultAddress: Address?, organizationType: OrganizationType, distanceInMeters: Long): List<Organization> {
        return try {
            val nativeSearchQuery = buildQueryForOrganizationWithSameTypeInDistance(defaultAddress, organizationType, distanceInMeters)
            search(nativeSearchQuery)
        } catch (ignored: IndexNotFoundException) {
            emptyList()
        }
    }

    override fun findByUrlName(urlName: String) = search(QueryBuilders.termQuery("urlName", urlName)).firstOrNull()

    override fun findOne(id: String) = search(QueryBuilders.idsQuery().addIds(id)).firstOrNull()

    override fun findOrganizationsByQuestionAnswersAndDistanceSortByAnswerMatchAndDistance(
        questionAnswers: List<QuestionAnswer>, position: GeoPoint, distance: Double
    ) = findOrganizationsWithQuestionsAndFilter(
        questionAnswers,
        filterDistance(position, distance)
    )
        .sortedWith(compareBy<ScoredOrganization> { it.score }.reversed().thenComparingDouble { it.organization.defaultAddress?.location?.distanceInKm(position) ?: Double.MAX_VALUE })

    override fun findOrganizationsByDistanceSortByDistance(position: GeoPoint, distance: Double) = search(filterDistance(position, distance))
        .sortedWith(compareBy { it.defaultAddress?.location?.distanceInKm(position) ?: Double.MAX_VALUE })

    override fun findGlobalOrganizationsByQuestionAnswersSortByAnswerMatch(questionAnswers: List<QuestionAnswer>) = findOrganizationsWithQuestionsAndFilter(
        questionAnswers,
        QueryBuilders.boolQuery()
            .mustNot(QueryBuilders.existsQuery("defaultAddress"))
    )
        .sortedBy { it.score }
        .reversed()

    override fun findGlobalOrganizations(): List<Organization> = search(
        QueryBuilders.boolQuery()
            .mustNot(QueryBuilders.existsQuery("defaultAddress"))
    )
        .sortedBy { it.name }

    override fun findGeoPointsOfOrganizationsInsideBoundingBox(position: GeoPoint?, distance: Double, boundingBox: BoundingBox): List<GeoPoint> {
        val boolQueryBuilder = QueryBuilders.boolQuery()
        val positionQuery = QueryBuilders.boolQuery()
            .must(filterBox(boundingBox))
        if (position != null) {
            positionQuery.mustNot(filterDistance(position, distance))
        }
        boolQueryBuilder.filter(positionQuery)
        val organizations = search(boolQueryBuilder)
        return extractOrganizations(emptyList(), organizations)
            .map { obj: ScoredOrganization -> obj.organization }
            .map { obj: Organization -> obj.defaultAddress }
            .filterNotNull()
            .map { it.location }
    }

    override fun save(organizations: List<Organization>) {
        val indexQueries = organizations
            .map {
                IndexQueryBuilder()
                    .withId(it.id.value)
                    .withObject(it)
            }
            .map { it.build() }
        elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of(indexName))
    }

    override fun remove(organizationId: OrganizationId) {
        elasticsearchOperations.delete(organizationId.value, IndexCoordinates.of(indexName))
    }

    fun createIndex(mapping: String?) {
        val indexOps = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName))
        if (!indexOps.exists()) {
            indexOps.create()
            if (mapping != null) {
                indexOps.putMapping(Document.parse(mapping))
            }
        }
    }

    private fun buildQueryForOrganizationWithSameTypeInDistance(defaultAddress: Address?, organizationType: OrganizationType, distanceInMeters: Long): BoolQueryBuilder {
        val organizationListQuery = QueryBuilders.boolQuery()
        organizationListQuery.must(QueryBuilders.termQuery("organizationType", organizationType.name))
        if (defaultAddress == null) {
            organizationListQuery.mustNot(QueryBuilders.existsQuery("defaultAddress"))
        } else {
            val locationToCheck = defaultAddress.location
            val geoDistanceQuery = QueryBuilders.geoDistanceQuery("defaultAddress.location")
                .point(locationToCheck.lat, locationToCheck.lon)
                .distance(distanceInMeters.toDouble(), DistanceUnit.METERS)
            organizationListQuery.must(geoDistanceQuery)
        }
        return organizationListQuery
    }

    private fun findOrganizationsWithQuestionsAndFilter(questionAnswers: List<QuestionAnswer>, filter: QueryBuilder): List<ScoredOrganization> {
        val boolQueryBuilder = QueryBuilders.boolQuery()
        questionAnswers
            .filter { Objects.nonNull(it.answer) }
            .map { buildQuestionQuery(it) }
            .forEach { boolQueryBuilder.should(it) }
        boolQueryBuilder.filter(filter)
        return extractOrganizations(
            questionAnswers,
            search(boolQueryBuilder)
        )
    }

    private fun buildQuestionQuery(questionAnswer: QuestionAnswer): QueryBuilder {
        val questionQuery = QueryBuilders.boolQuery()
            .minimumShouldMatch(1)
            .must(QueryBuilders.termQuery("questions.questionId", questionAnswer.questionId.value))
            .should(QueryBuilders.termQuery("questions.answer", questionAnswer.answer.toString()).boost(2.0f))
        for (neighbour in questionAnswer.answer.neighbours) {
            questionQuery.should(QueryBuilders.termQuery("questions.answer", neighbour.toString()).boost(1.0f))
        }
        return QueryBuilders.nestedQuery("questions", questionQuery, ScoreMode.Max)
    }

    private fun filterBox(boundingBoxDto: BoundingBox): GeoBoundingBoxQueryBuilder {
        val topRight = boundingBoxDto.northEast
        val bottomLeft = boundingBoxDto.southWest
        return QueryBuilders.geoBoundingBoxQuery("defaultAddress.location")
            .setCornersOGC(bottomLeft.toElasticsearchGeoPoint(), topRight.toElasticsearchGeoPoint())
    }

    private fun filterDistance(position: GeoPoint, distance: Double): GeoDistanceQueryBuilder {
        return QueryBuilders.geoDistanceQuery("defaultAddress.location")
            .point(position.toElasticsearchGeoPoint())
            .distance(distance, DistanceUnit.KILOMETERS)
    }

    private fun extractOrganizations(questionAnswers: List<QuestionAnswer>, resultOrganizations: List<Organization>): List<ScoredOrganization> {
        return resultOrganizations
            .map { ScoredOrganization(it, calculateScore(it.questionAnswers, questionAnswers)) }
    }

    private fun calculateScore(organizationQuestions: List<QuestionAnswer>, questionAnswers: List<QuestionAnswer>): Double {
        return organizationQuestions
            .map {
                val questionId = it.questionId
                val organizationAnswer = it.answer
                val userAnswer = questionAnswers
                    .filter { questionAnswer: QuestionAnswer -> questionAnswer.questionId == questionId }
                    .map { it.answer }
                    .firstOrNull { Objects.nonNull(it) }
                    ?: return@map null
                if (organizationAnswer == userAnswer) {
                    100.0
                } else if (organizationAnswer.neighbours.contains(userAnswer)) {
                    50.0
                } else {
                    0.0
                }
            }
            .filterNotNull()
            .average()
    }

    private fun search(query: QueryBuilder): List<Organization> {
        val nativeSearchQuery = NativeSearchQuery(query)
        nativeSearchQuery.setPageable<Query>(PageRequest.of(0, DEFAULT_MAX_RESULT_SIZE))
        val result = arrayListOf<Organization>()
        var stream: SearchHitsIterator<Organization>? = null
        try {
            stream = elasticsearchOperations.searchForStream(nativeSearchQuery, Organization::class.java, IndexCoordinates.of(indexName))
            for (searchHit in stream) {
                result.add(searchHit.content)
            }
        } finally {
            stream?.close()
        }
        return result
    }

    companion object {
        private const val DEFAULT_MAX_RESULT_SIZE = 10000
    }

}

fun GeoPoint.toElasticsearchGeoPoint() = org.elasticsearch.common.geo.GeoPoint(this.lat, this.lon)
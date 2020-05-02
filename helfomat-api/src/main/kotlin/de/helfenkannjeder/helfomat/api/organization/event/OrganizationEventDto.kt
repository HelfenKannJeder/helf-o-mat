package de.helfenkannjeder.helfomat.api.organization.event

import com.fasterxml.jackson.annotation.JsonTypeInfo
import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.MINIMAL_CLASS, property = "type")
interface OrganizationEventDto {

    val organizationId: OrganizationId

    fun <T> visit(visitor: OrganizationEventDtoVisitor<T>): T

}
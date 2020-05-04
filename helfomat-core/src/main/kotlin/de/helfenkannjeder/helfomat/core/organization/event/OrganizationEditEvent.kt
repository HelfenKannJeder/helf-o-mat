package de.helfenkannjeder.helfomat.core.organization.event

import de.helfenkannjeder.helfomat.core.organization.OrganizationId

/**
 * @author Valentin Zickner
 */
abstract class OrganizationEditEvent(organizationId: OrganizationId) : OrganizationEvent(organizationId) {

    protected fun <T> changePosition(items: MutableList<T>, oldElement: T, newElement: T, indexOffset: Int): MutableList<T> {
        val indexOf = items.indexOf(oldElement);
        if (indexOf != -1) {
            items.removeAt(indexOf)
            items.add(indexOf + indexOffset, newElement)
        }
        return items
    }

}
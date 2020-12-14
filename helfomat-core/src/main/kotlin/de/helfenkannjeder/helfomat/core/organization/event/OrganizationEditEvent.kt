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
            val newIndex = indexOf + indexOffset
            if (newIndex > items.size || newIndex < 0) {
                items.add(newElement)
            } else {
                items.add(newIndex, newElement)
            }
        }
        return items
    }

}
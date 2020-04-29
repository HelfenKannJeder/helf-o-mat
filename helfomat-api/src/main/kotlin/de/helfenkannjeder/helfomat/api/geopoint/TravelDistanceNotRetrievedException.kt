package de.helfenkannjeder.helfomat.api.geopoint

/**
 * @author Valentin Zickner
 */
class TravelDistanceNotRetrievedException : RuntimeException {
    constructor(message: String?) : super(message)
    constructor(cause: Throwable?) : super("Travel distance can not be retrieved.", cause)
}
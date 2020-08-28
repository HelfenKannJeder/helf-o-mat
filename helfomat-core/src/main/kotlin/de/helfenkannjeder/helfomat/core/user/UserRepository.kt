package de.helfenkannjeder.helfomat.core.user

interface UserRepository {

    fun findByUsername(username: String): User?

}
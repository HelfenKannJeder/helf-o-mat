package de.helfenkannjeder.helfomat

import de.helfenkannjeder.helfomat.core.user.User
import de.helfenkannjeder.helfomat.core.user.UserRepository
import org.springframework.stereotype.Service

@Service
class MockUserRepository : UserRepository {
    override fun findByUsername(username: String): User? = null
}
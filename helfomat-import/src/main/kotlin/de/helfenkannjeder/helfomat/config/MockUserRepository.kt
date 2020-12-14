package de.helfenkannjeder.helfomat.config

import de.helfenkannjeder.helfomat.core.user.User
import de.helfenkannjeder.helfomat.core.user.UserRepository
import org.springframework.context.annotation.Configuration

@Configuration
open class MockUserRepository : UserRepository {
    override fun findByUsername(username: String): User? {
        return null
    }
}
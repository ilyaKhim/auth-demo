package ru.ilkhim.persistence.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.ilkhim.persistence.model.User

interface UserRepository : JpaRepository<User, Int> {
    fun findUserByEmail(email: String): User?

    fun existsUserByEmail(email: String?): Boolean
}

package ru.ilkhim.service

import ru.ilkhim.persistence.model.User

interface UserService {
    fun createUser(user: User): User

    fun findUserByEmail(email: String): User
}

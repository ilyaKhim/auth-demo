package ru.ilkhim.service

import ru.ilkhim.persistence.model.User

interface SmartmemoService {
    fun createCoachAndReceiveCoachId(user: User): Int
}

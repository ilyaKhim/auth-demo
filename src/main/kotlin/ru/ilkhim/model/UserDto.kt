package ru.ilkhim.model

import java.io.Serializable

class UserDto : Serializable {
    lateinit var firstName: String
    lateinit var lastName: String
    lateinit var email: String
}

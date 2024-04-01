package ru.ilkhim.persistence.model

import ru.ilkhim.model.Role
import java.io.Serializable

data class UserRoleId(
    var user: User? = null,
    var role: Role = Role.USER,
) : Serializable

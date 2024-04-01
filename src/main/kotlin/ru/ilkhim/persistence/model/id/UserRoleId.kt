package ru.ilkhim.persistence.model.id

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import ru.ilkhim.persistence.model.User.*
import java.io.Serializable

@Embeddable
class UserRoleId : Serializable {
    @Column(name = "user_id", nullable = false)
    var userId: Int = 0

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    lateinit var role: Role
}

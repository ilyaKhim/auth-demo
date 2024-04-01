package ru.ilkhim.persistence.model

import jakarta.persistence.*
import ru.ilkhim.model.Role

@Entity
@Table(name = "user_role", schema = "auth")
@IdClass(UserRoleId::class)
class UserRole() {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    lateinit var user: User

    @Id
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    lateinit var role: Role

    constructor(user: User, role: Role) : this() {
        this.user = user
        this.role = role
    }
}

package ru.ilkhim.persistence.model

import jakarta.persistence.*
import java.time.ZonedDateTime

@Entity
@Table(name = "user", schema = "auth")
class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    var id: Int = 0

    @Column(name = "email")
    var email: String? = null

    @Column(name = "first_name")
    var firstName: String? = null

    @Column(name = "last_name")
    var lastName: String? = null

    @Column(name = "password")
    var password: String? = null

    @OneToMany(mappedBy = "user", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var roles: Set<UserRole> = mutableSetOf()

    @Column(name = "create_time")
    var createTime: ZonedDateTime? = null

    @Column(name = "update_time")
    var updateTime: ZonedDateTime? = null

    @PrePersist
    fun prePersist() {
        createTime = ZonedDateTime.now()
        updateTime = ZonedDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updateTime = ZonedDateTime.now()
    }
}

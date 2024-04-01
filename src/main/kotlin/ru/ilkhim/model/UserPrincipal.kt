package ru.ilkhim.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserPrincipal : UserDetails {
    lateinit var email: String
    var firstName: String? = null
    var lastName: String? = null
    var userPassword: String? = null
    lateinit var roles: Set<Role>
    var coachId: Int? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles.map { SimpleGrantedAuthority(it.name) }.toMutableList()
    }

    override fun getPassword(): String? {
        return userPassword
    }

    override fun getUsername(): String {
        return email
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun toString(): String {
        return "UserPrincipal(email='$email', firstName=$firstName, lastName=$lastName, roles=$roles, coachId=$coachId)"
    }
}

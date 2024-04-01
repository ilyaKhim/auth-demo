package ru.ilkhim.model

import org.springframework.security.core.GrantedAuthority

enum class Role(
    private val value: String,
) : GrantedAuthority {
    ADMIN("ADMIN"),
    USER("USER"),
    ;

    override fun getAuthority(): String {
        return value
    }
}

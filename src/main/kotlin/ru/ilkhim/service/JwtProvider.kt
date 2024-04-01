package ru.ilkhim.service

import io.jsonwebtoken.Claims
import ru.ilkhim.model.UserPrincipal

interface JwtProvider {
    fun generateAccessToken(userPrincipal: UserPrincipal): String

    fun generateRefreshToken(userPrincipal: UserPrincipal): String

    fun getAccessClaims(accessToken: String): Claims

    fun getRefreshClaims(refreshToken: String): Claims

    fun getRolesFromToken(accessClaims: Claims): List<String>
}

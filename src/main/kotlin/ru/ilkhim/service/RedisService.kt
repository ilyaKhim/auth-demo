package ru.ilkhim.service

interface RedisService {
    fun storeToken(
        username: String,
        refreshToken: String,
    )

    fun findToken(username: String): String?

    fun revokeAllTokens(username: String)
}

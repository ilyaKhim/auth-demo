package ru.ilkhim.service

import ru.ilkhim.model.CookiePair
import ru.ilkhim.model.HeaderPair
import ru.ilkhim.model.UserPrincipal

interface JwtService {
    fun generateCookiePair(userPrincipal: UserPrincipal): CookiePair

    fun provideHeaderPair(accessToken: String): HeaderPair

    fun recreateTokenPair(refreshToken: String): CookiePair
}

package ru.ilkhim.service.impl

import io.jsonwebtoken.JwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ilkhim.model.CookiePair
import ru.ilkhim.model.HeaderPair
import ru.ilkhim.model.UserPrincipal
import ru.ilkhim.service.JwtProvider
import ru.ilkhim.service.JwtService
import ru.ilkhim.service.RedisService
import ru.ilkhim.service.UserPrincipalService

@Service
class JwtServiceImpl(
    @Autowired
    private val jwtProvider: JwtProvider,
    @Autowired
    private val userPrincipalService: UserPrincipalService,
    @Autowired
    private val redisService: RedisService,
) : JwtService {
    override fun generateCookiePair(userPrincipal: UserPrincipal): CookiePair {
        val accessToken = jwtProvider.generateAccessToken(userPrincipal)
        val refreshToken = jwtProvider.generateRefreshToken(userPrincipal)
        redisService.storeToken(userPrincipal.username, refreshToken)
        return CookiePair(accessToken, refreshToken)
    }

    override fun provideHeaderPair(accessToken: String): HeaderPair {
        val accessClaims = jwtProvider.getAccessClaims(accessToken)
        val userPrincipal = userPrincipalService.loadUserByUsername(accessClaims.subject)
        return HeaderPair(userPrincipal.coachId.toString(), jwtProvider.getRolesFromToken(accessClaims))
    }

    override fun recreateTokenPair(refreshToken: String): CookiePair {
        val refreshClaims = jwtProvider.getRefreshClaims(refreshToken)
        val username = refreshClaims.subject
        val userPrincipal = userPrincipalService.loadUserByUsername(username)
        val savedRefreshToken = redisService.findToken(username)
        if (savedRefreshToken != null && savedRefreshToken == refreshToken) {
            val newAccessToken = jwtProvider.generateAccessToken(userPrincipal)
            val newRefreshToken = jwtProvider.generateRefreshToken(userPrincipal)
            redisService.revokeAllTokens(username)
            redisService.storeToken(username, newRefreshToken)
            return CookiePair(newAccessToken, newRefreshToken)
        }
        throw JwtException("Refresh token is not found/expired")
    }
}

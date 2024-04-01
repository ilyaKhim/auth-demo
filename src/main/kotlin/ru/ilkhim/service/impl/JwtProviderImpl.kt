package ru.ilkhim.service.impl

import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.ilkhim.model.UserPrincipal
import ru.ilkhim.service.JwtProvider
import java.security.Key
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtProviderImpl(
    @Value("\${jwt.secret.access}") jwtAccessSecret: String,
    @Value("\${jwt.secret.refresh}") jwtRefreshSecret: String,
    @Value("\${jwt.accessToken.lifetime}") private val jwtAccessTokenLifetimeMinutes: Long,
    @Value("\${jwt.refreshToken.lifetime}") private val jwtRefreshTokenLifetimeDays: Long,
) : JwtProvider {
    private val jwtAccessSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtAccessSecret))
    private val jwtRefreshSecret: SecretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtRefreshSecret))

    override fun generateAccessToken(userPrincipal: UserPrincipal): String {
        val now: LocalDateTime = LocalDateTime.now()
        val accessExpirationInstant =
            now
                .plusMinutes(jwtAccessTokenLifetimeMinutes)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .claim(rolesClaim, aggregateUserRoles(userPrincipal))
            .setExpiration(Date.from(accessExpirationInstant))
            .signWith(jwtAccessSecret)
            .compact()
    }

    override fun generateRefreshToken(userPrincipal: UserPrincipal): String {
        val now = LocalDateTime.now()
        val refreshExpirationInstant =
            now
                .plusDays(jwtRefreshTokenLifetimeDays)
                .atZone(ZoneId.systemDefault())
                .toInstant()
        return Jwts.builder()
            .setSubject(userPrincipal.username)
            .claim(rolesClaim, aggregateUserRoles(userPrincipal))
            .setExpiration(Date.from(refreshExpirationInstant))
            .signWith(jwtRefreshSecret)
            .compact()
    }

    override fun getAccessClaims(accessToken: String): Claims {
        return getClaims(accessToken, jwtAccessSecret)
    }

    override fun getRefreshClaims(refreshToken: String): Claims {
        return getClaims(refreshToken, jwtRefreshSecret)
    }

    override fun getRolesFromToken(accessClaims: Claims): List<String> {
        return (accessClaims[rolesClaim] as String).split(",")
    }

    private fun getClaims(
        token: String,
        secret: Key,
    ): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(secret)
            .build()
            .parseClaimsJws(token)
            .body
    }

    private fun aggregateUserRoles(userPrincipal: UserPrincipal) = userPrincipal.roles.joinToString(",")

    companion object {
        private const val rolesClaim = "roles"
    }
}

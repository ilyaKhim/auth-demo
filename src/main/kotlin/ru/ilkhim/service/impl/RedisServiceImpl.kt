package ru.ilkhim.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import ru.ilkhim.service.RedisService
import java.util.concurrent.TimeUnit

@Service
class RedisServiceImpl(
    @Autowired
    private val redisTemplate: RedisTemplate<String, String>,
    @Value("\${jwt.refreshToken.lifetime}") private val jwtRefreshTokenLifetimeDays: Long,
) : RedisService {
    private val valueOperations: ValueOperations<String, String> = redisTemplate.opsForValue()

    override fun storeToken(
        username: String,
        refreshToken: String,
    ) {
        valueOperations.set(username, refreshToken, jwtRefreshTokenLifetimeDays, TimeUnit.DAYS)
    }

    override fun findToken(username: String): String? {
        return valueOperations.get(username)
    }

    override fun revokeAllTokens(username: String) {
        valueOperations.getAndDelete(username)
    }
}

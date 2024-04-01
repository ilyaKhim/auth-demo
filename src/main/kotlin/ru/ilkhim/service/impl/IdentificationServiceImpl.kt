package ru.ilkhim.service.impl

import io.jsonwebtoken.ExpiredJwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ilkhim.model.CookiePair
import ru.ilkhim.model.HeaderPair
import ru.ilkhim.service.IdentificationService
import ru.ilkhim.service.JwtService

@Service
class IdentificationServiceImpl(
    @Autowired
    private val jwtService: JwtService,
) : IdentificationService {
    override fun identify(
        accessToken: String,
        refreshToken: String,
    ): Pair<CookiePair, HeaderPair> {
        var headerPair: HeaderPair
        var cookiePair = CookiePair(accessToken, refreshToken)
        try {
            headerPair = jwtService.provideHeaderPair(accessToken)
        } catch (e: ExpiredJwtException) {
            cookiePair = jwtService.recreateTokenPair(refreshToken)
            headerPair = jwtService.provideHeaderPair(cookiePair.accessToken)
        }
        return Pair(cookiePair, headerPair)
    }
}

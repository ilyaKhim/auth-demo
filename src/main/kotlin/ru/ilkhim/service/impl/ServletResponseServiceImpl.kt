package ru.ilkhim.service.impl

import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import ru.ilkhim.model.CookiePair
import ru.ilkhim.model.HeaderPair
import ru.ilkhim.service.ServletResponseService

@Service
class ServletResponseServiceImpl : ServletResponseService {
    override fun addCookiesToResponse(
        response: HttpServletResponse,
        cookiePair: CookiePair,
    ) {
        val accessCookie = createCookie(accessToken, cookiePair.accessToken)
        response.addCookie(accessCookie)
        val refreshCookie = createCookie(refreshToken, cookiePair.refreshToken)
        response.addCookie(refreshCookie)
    }

    override fun addHeadersToResponse(
        response: HttpServletResponse,
        headerPair: HeaderPair,
    ) {
        response.addHeader(coachHeader, headerPair.coachId)
        headerPair.roles.forEach { response.addHeader(roleHeader, it) }
    }

    private fun createCookie(
        name: String,
        value: String,
    ): Cookie {
        val cookie = Cookie(name, value)
        cookie.isHttpOnly = true
        cookie.secure = true
        cookie.path = cookiePath
        cookie.setAttribute("SameSite", "None")
        return cookie
    }

    companion object {
        private const val cookiePath = "/"
        private const val accessToken = "access-token"
        private const val refreshToken = "refresh-token"
        private const val coachHeader = "x-smartmemo-coach"
        private const val roleHeader = "x-smartmemo-role"
    }
}

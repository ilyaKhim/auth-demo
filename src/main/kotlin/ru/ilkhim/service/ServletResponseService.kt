package ru.ilkhim.service

import jakarta.servlet.http.HttpServletResponse
import ru.ilkhim.model.CookiePair
import ru.ilkhim.model.HeaderPair

interface ServletResponseService {
    fun addCookiesToResponse(
        response: HttpServletResponse,
        cookiePair: CookiePair,
    )

    fun addHeadersToResponse(
        response: HttpServletResponse,
        headerPair: HeaderPair,
    )
}

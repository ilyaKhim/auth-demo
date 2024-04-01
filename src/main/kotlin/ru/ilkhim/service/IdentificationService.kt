package ru.ilkhim.service

import ru.ilkhim.model.CookiePair
import ru.ilkhim.model.HeaderPair

interface IdentificationService {
    fun identify(
        accessToken: String,
        refreshToken: String,
    ): Pair<CookiePair, HeaderPair>
}

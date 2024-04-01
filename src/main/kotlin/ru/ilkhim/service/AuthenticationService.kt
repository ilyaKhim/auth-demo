package ru.ilkhim.service

import ru.ilkhim.model.CookiePair
import ru.ilkhim.web.model.request.CreateUserRequestDto
import ru.ilkhim.web.model.request.LoginUserRequestDto

interface AuthenticationService {
    fun register(createUserRequestDto: CreateUserRequestDto): CookiePair

    fun authenticate(loginUserRequestDto: LoginUserRequestDto): CookiePair
}

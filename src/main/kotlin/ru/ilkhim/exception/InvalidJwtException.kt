package ru.ilkhim.exception

import io.jsonwebtoken.JwtException

class InvalidJwtException(override val message: String = "Jwt doesn't belong current user") :
    JwtException(message)

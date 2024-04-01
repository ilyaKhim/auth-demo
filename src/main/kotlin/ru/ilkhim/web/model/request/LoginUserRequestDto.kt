package ru.ilkhim.web.model.request

import jakarta.validation.constraints.NotBlank

class LoginUserRequestDto {
    @NotBlank
    lateinit var email: String

    @NotBlank
    lateinit var password: String
}

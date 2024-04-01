package ru.ilkhim.web.model.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

class CreateUserRequestDto {
    @Email(
        regexp = "^[a-zA-Z\\d_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+\$",
        message = "Email should be valid and match the pattern '^[a-zA-Z\\d_!#\$%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+\$'",
    )
    lateinit var email: String

    @NotBlank
    lateinit var firstName: String

    @NotBlank
    lateinit var lastName: String

    @Pattern(
        regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*(),.?:{}|<>]).{12,24}$",
        message = "Password must be between 12 and 24 characters, and include at least one digit, one lowercase letter, one uppercase letter, and one special character from !@#$%^&*(),.?:{}|<>",
    )
    lateinit var password: String
}

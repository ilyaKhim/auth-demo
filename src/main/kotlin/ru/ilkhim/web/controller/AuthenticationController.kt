package ru.ilkhim.web.controller

import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springdoc.core.fn.builders.exampleobject.Builder.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.ilkhim.service.AuthenticationService
import ru.ilkhim.service.ServletResponseService
import ru.ilkhim.web.model.request.CreateUserRequestDto
import ru.ilkhim.web.model.request.LoginUserRequestDto

@RestController
@RequestMapping("/api/v1/auth")
class AuthenticationController(
    @Autowired
    private val authenticationService: AuthenticationService,
    @Autowired
    private val servletResponseService: ServletResponseService,
) {
    @PostMapping("register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "No Content",
            content = [Content()],
            headers = [
                Header(
                    name = "Set-Cookie",
                ),
            ],
        ),
        ApiResponse(
            responseCode = "400",
            description = "Bad request",
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "MethodArgumentNotValidException",
                            summary = "Невалидные входные данные",
                            value = """
                    {
                        "exception": "MethodArgumentNotValidException",
                        "message": "password: Password must be between 12 and 24 characters, and include at least one digit, one lowercase letter, one uppercase letter, and one special character from !@#${'$'}%^&*(),.?:{}|<>, email: Email should be valid and match the pattern '^[a-zA-Z\\d_!#${'$'}%&'*+/=?`{|}~^.-]+@[a-zA-Z\\d.-]+${'$'}'"
                    }
                """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "409",
            description = "Conflict",
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "UserAlreadyExistsException",
                            summary = "Учётная запись с данным email'ом уже существует",
                            value = """
                    {
                        "exception": "UserAlreadyExistsException",
                        "message": "User with user@example.com already exists"
                    }
                """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "SmartmemoException Bad Response",
                            summary = "Ошибка при взаимодействии с модулем smartmemo-core. (Smartmemo не вернул coachId)",
                            value = """
                    {
                        "exception": "SmartmemoException",
                        "message": "Exception during interacting with smartmemo. Response doesn't contain coachId"
                    }
                """,
                        ),
                        ExampleObject(
                            name = "SmartmemoException No Response",
                            summary = "Ошибка при взаимодействии с модулем smartmemo-core. (Smartmemo отвечал слишком долго/выбросил ошибку)",
                            value = """
                    {
                        "exception": "SmartmemoException",
                        "message": "Exception during interacting with smartmemo. Unexpected response"
                    }
                """,
                        ),
                        ExampleObject(
                            name = "Exception",
                            summary = "Непредвиденная ошибка.",
                            value = """
                    {
                        "exception": "Exception",
                        "message": "Unexpected exception"
                    }
                """,
                        ),
                    ],
                ),
            ],
        ),
    )
    fun register(
        @RequestBody @Valid createUserRequestDto: CreateUserRequestDto,
        response: HttpServletResponse,
    ) {
        val cookiePair = authenticationService.register(createUserRequestDto)
        servletResponseService.addCookiesToResponse(response, cookiePair)
    }

    @PostMapping("authenticate")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
        ApiResponse(
            responseCode = "204",
            description = "No Content",
            content = [Content()],
            headers = [
                Header(
                    name = "Set-Cookie",
                ),
            ],
        ),
        ApiResponse(
            responseCode = "401",
            description = "Unauthorized status",
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "BadCredentialsException",
                            summary = "Неверный логин и/или пароль",
                            value = """
                    {
                        "exception": "BadCredentialsException",
                        "message": "Bad credentials"
                    }
                """,
                        ),
                    ],
                ),
            ],
        ),
        ApiResponse(
            responseCode = "500",
            description = "Internal Server Error",
            content = [
                Content(
                    mediaType = "application/json",
                    examples = [
                        ExampleObject(
                            name = "Exception",
                            summary = "Непредвиденная ошибка.",
                            value = """
                    {
                        "exception": "Exception",
                        "message": "Unexpected exception"
                    }
                """,
                        ),
                    ],
                ),
            ],
        ),
    )
    fun login(
        @RequestBody @Valid loginUserRequestDto: LoginUserRequestDto,
        response: HttpServletResponse,
    ) {
        val cookiePair = authenticationService.authenticate(loginUserRequestDto)
        servletResponseService.addCookiesToResponse(response, cookiePair)
    }
}

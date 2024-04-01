package ru.ilkhim.web.controller

import io.swagger.v3.oas.annotations.headers.Header
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.ilkhim.service.IdentificationService
import ru.ilkhim.service.ServletResponseService

@RestController
@RequestMapping("/api/v1/jwt")
class TokenController(
    @Autowired
    private val identificationService: IdentificationService,
    @Autowired
    private val servletResponseService: ServletResponseService,
) {
    @GetMapping("identify")
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
                Header(
                    name = "x-smartmemo-coach",
                ),
                Header(
                    name = "x-smartmemo-role",
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
                            name = "JwtException (Expired)",
                            summary = "Access и refresh токены просрочены",
                            value = """
                    {
                        "exception": "JwtException",
                        "message": "JWT expired at 2023-10-18T19:05:42Z. Current time: 2023-10-18T19:05:42Z, a difference of 880 milliseconds.  Allowed clock skew: 0 milliseconds."
                    }
                """,
                        ),
                        ExampleObject(
                            name = "JwtException (Malformed)",
                            summary = "Access/refresh токен подделан/невалидный",
                            value = """
                    {
                        "exception": "JwtException",
                        "message": "JWT signature does not match locally computed signature. JWT validity cannot be asserted and should not be trusted."
                    }
                """,
                        ),
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
    fun identifyUser(
        @CookieValue("access-token") accessToken: String,
        @CookieValue("refresh-token") refreshToken: String,
        response: HttpServletResponse,
    ) {
        val (cookiePair, headerPair) = identificationService.identify(accessToken, refreshToken)
        servletResponseService.addCookiesToResponse(response, cookiePair)
        servletResponseService.addHeadersToResponse(response, headerPair)
    }
}

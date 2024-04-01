package ru.ilkhim.config

import io.jsonwebtoken.JwtException
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.ilkhim.exception.SmartmemoException
import ru.ilkhim.exception.UserAlreadyExistsException
import java.io.Serializable

@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {
    override fun handleMethodArgumentNotValid(
        ex: org.springframework.web.bind.MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest,
    ): ResponseEntity<Any>? {
        val concatenatedErrors =
            ex.bindingResult.fieldErrors.joinToString(", ") {
                "${it.field}: ${it.defaultMessage}"
            }
        return ResponseEntity(
            ru.ilkhim.config.GlobalExceptionHandler.ErrorDetails(
                ex.javaClass.simpleName,
                concatenatedErrors
            ), headers, status)
    }

    @ExceptionHandler(JwtException::class)
    fun handleJwtException(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<Any> {
        return ResponseEntity(
            ru.ilkhim.config.GlobalExceptionHandler.ErrorDetails(
                JwtException::class.simpleName,
                ex.message
            ), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExistsException(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<Any> {
        return ResponseEntity(ru.ilkhim.config.GlobalExceptionHandler.ErrorDetails(ex.javaClass.simpleName, ex.message), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(SmartmemoException::class)
    fun handleSmartmemoException(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<Any> {
        return ResponseEntity(ru.ilkhim.config.GlobalExceptionHandler.ErrorDetails(ex.javaClass.simpleName, ex.message), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<Any> {
        return ResponseEntity(ru.ilkhim.config.GlobalExceptionHandler.ErrorDetails(ex.javaClass.simpleName, ex.message), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<Any> {
        return ResponseEntity(
            ru.ilkhim.config.GlobalExceptionHandler.ErrorDetails(
                Exception::class.simpleName,
                "Unexpected exception"
            ), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    class ErrorDetails(val exception: String?, val message: String?) : Serializable
}

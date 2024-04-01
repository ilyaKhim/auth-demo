package ru.ilkhim.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class ApiKeyValidationFilter(
    @Value("\${api.key.header}")
    private val apiKeyHeader: String,
    @Value("\${api.key}")
    private val validApiKey: String,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        val apiKey = request.getHeader(apiKeyHeader)

        if (!StringUtils.equals(apiKey, validApiKey)) {
            response.status = HttpStatus.FORBIDDEN.value()
            response.writer.write("Invalid or missing API key.")
            return
        }

        filterChain.doFilter(request, response)
    }
}

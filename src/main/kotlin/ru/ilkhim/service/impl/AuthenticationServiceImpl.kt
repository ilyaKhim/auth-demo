package ru.ilkhim.service.impl

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.stereotype.Service
import ru.ilkhim.model.CookiePair
import ru.ilkhim.service.AuthenticationService
import ru.ilkhim.service.JwtService
import ru.ilkhim.service.UserMapper
import ru.ilkhim.service.UserPrincipalService
import ru.ilkhim.web.model.request.CreateUserRequestDto
import ru.ilkhim.web.model.request.LoginUserRequestDto

@Service
class AuthenticationServiceImpl(
    @Autowired
    private val userPrincipalService: UserPrincipalService,
    @Autowired
    private val jwtService: JwtService,
    @Autowired
    private val authenticationManager: AuthenticationManager,
    @Autowired
    private val userMapper: UserMapper,
) : AuthenticationService {
    override fun register(createUserRequestDto: CreateUserRequestDto): CookiePair {
        val userPrincipal = userPrincipalService.createUserPrincipal(userMapper.toUserPrincipal(createUserRequestDto))
        return jwtService.generateCookiePair(userPrincipal)
    }

    override fun authenticate(loginUserRequestDto: LoginUserRequestDto): CookiePair {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginUserRequestDto.email,
                loginUserRequestDto.password,
            ),
        )
        val userPrincipal = userPrincipalService.loadUserByUsername(loginUserRequestDto.email)
        return jwtService.generateCookiePair(userPrincipal)
    }
}

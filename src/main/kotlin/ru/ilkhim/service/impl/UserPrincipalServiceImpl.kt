package ru.ilkhim.service.impl

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.ilkhim.model.UserPrincipal
import ru.ilkhim.service.UserMapper
import ru.ilkhim.service.UserPrincipalService
import ru.ilkhim.service.UserService

@Service
class UserPrincipalServiceImpl(
    @Autowired
    private val userService: UserService,
    @Autowired
    private val userMapper: UserMapper,
) : UserPrincipalService {
    @Transactional
    override fun createUserPrincipal(userPrincipal: UserPrincipal): UserPrincipal {
        val user = userMapper.toUser(userPrincipal)
        return userMapper.toUserPrincipal(userService.createUser(user))
    }

    override fun loadUserByUsername(email: String): UserPrincipal {
        return userMapper.toUserPrincipal(userService.findUserByEmail(email))
    }
}

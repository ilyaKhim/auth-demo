package ru.ilkhim.service.impl

import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import ru.ilkhim.exception.UserAlreadyExistsException
import ru.ilkhim.persistence.model.User
import ru.ilkhim.persistence.repository.UserRepository
import ru.ilkhim.service.SmartmemoService
import ru.ilkhim.service.UserService

@Service
class UserServiceImpl(
    @Autowired
    private val userRepository: UserRepository,
    @Autowired
    private val smartmemoService: SmartmemoService,
) : UserService {
    @Transactional(value = Transactional.TxType.REQUIRED, rollbackOn = [Exception::class])
    override fun createUser(user: User): User {
        if (userRepository.existsUserByEmail(user.email)) {
            throw UserAlreadyExistsException(user.email)
        }
        return userRepository.save(user)
    }

    override fun findUserByEmail(email: String): User {
        return userRepository.findUserByEmail(email)
            ?: throw UsernameNotFoundException("User with email:$email not found")
    }
}

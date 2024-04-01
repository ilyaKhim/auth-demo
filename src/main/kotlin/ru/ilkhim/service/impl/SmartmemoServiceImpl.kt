package ru.ilkhim.service.impl

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Service
import ru.ilkhim.exception.SmartmemoException
import ru.ilkhim.model.CoachCreatedSuccessfullyEvent
import ru.ilkhim.model.UserDto
import ru.ilkhim.persistence.model.User
import ru.ilkhim.service.SmartmemoService
import ru.ilkhim.service.UserMapper

@Service
class SmartmemoServiceImpl(
    @Autowired
    @Qualifier("smartmemoRabbitTemplate")
    private val rabbitTemplate: RabbitTemplate,
    @Autowired
    private val userMapper: UserMapper,
    @Value("\${spring.rabbitmq.queue.coach.create.routingkey}")
    private val createCoachRoutingKey: String,
    @Value("\${spring.rabbitmq.queue.coach.remove.routingkey}")
    private val removeCoachRoutingQueue: String,
) : SmartmemoService {
    override fun createCoachAndReceiveCoachId(user: User): Int {
        val userDto = userMapper.toUserDto(user)
        val smartmemoResponse =
            rabbitTemplate.convertSendAndReceiveAsType(
                createCoachRoutingKey,
                userDto,
                object : ParameterizedTypeReference<CoachCreatedSuccessfullyEvent>() {},
            )

        if (smartmemoResponse is CoachCreatedSuccessfullyEvent) {
            val coachId = smartmemoResponse.coachId
            if (coachId == null) {
                removeUnsuccessfullyCreatedCoach(userDto)
                throw SmartmemoException("Response doesn't contain coachId")
            }
            return coachId
        }
        removeUnsuccessfullyCreatedCoach(userDto)
        throw SmartmemoException("Unexpected response")
    }

    private fun removeUnsuccessfullyCreatedCoach(userDto: UserDto) {
        rabbitTemplate.convertAndSend(
            removeCoachRoutingQueue,
            userDto,
        )
    }
}

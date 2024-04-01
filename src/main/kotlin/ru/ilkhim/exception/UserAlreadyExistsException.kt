package ru.ilkhim.exception

class UserAlreadyExistsException(val email: String?, override val message: String = "User with $email already exists") :
    RuntimeException(message)

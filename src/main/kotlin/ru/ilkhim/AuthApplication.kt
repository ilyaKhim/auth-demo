package ru.ilkhim

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AuthApplication

fun main(args: Array<String>) {
    runApplication<ru.ilkhim.AuthApplication>(*args)
}

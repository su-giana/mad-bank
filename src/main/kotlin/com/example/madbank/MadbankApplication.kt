package com.example.madbank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MadbankApplication

fun main(args: Array<String>) {
    runApplication<MadbankApplication>(*args)
}

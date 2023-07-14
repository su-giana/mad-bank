package com.example.madbank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class GlobalExceptionHanlder: ResponseEntityExceptionHandler()
{
    @ExceptionHandler(Exception::class)
    fun handlerException(ex: Exception, request: WebRequest): ResponseEntity<Any>
    {
        ex.printStackTrace()
        val body = "Error occurred, check terminal"
        return handleExceptionInternal(ex, body, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }
}

@SpringBootApplication
class MadbankApplication

fun main(args: Array<String>) {
    runApplication<MadbankApplication>(*args)
}

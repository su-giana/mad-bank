package com.example.madbank.user_exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class NotExistingUserException(message : String) : Exception(message) {
}

class PasswordNotMatchesException(message : String) : Exception(message)
{

}

class NotValidTokenException(message:String):Exception(message){}

class AlreadyRegisteredException(message:String):Exception(message){}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotExistingUserException::class)
    fun handleNotExistingUserException(ex: NotExistingUserException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(PasswordNotMatchesException::class)
    fun handlePasswordNotMatchesException(ex: PasswordNotMatchesException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(NotValidTokenException::class)
    fun handleNotValidTokenException(ex: NotValidTokenException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(AlreadyRegisteredException::class)
    fun handleAlreadyRegisteredException(ex: AlreadyRegisteredException): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<String> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred")
    }
}

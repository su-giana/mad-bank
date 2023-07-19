package com.example.madbank.user_exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

class NotExistingUserException(message : String) : RuntimeException(message) {}

class PasswordNotMatchesException(message : String) : RuntimeException(message) {}

class NotValidTokenException(message:String):RuntimeException(message){}

class AlreadyRegisteredException(message:String):RuntimeException(message){}

class BankAccountNotExist(message: String):RuntimeException(message){}

class NotValidTransferException(message: String):RuntimeException(message){}

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(NotExistingUserException::class)
    fun handleNotExistingUserException(ex: NotExistingUserException): ResponseEntity<String> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(PasswordNotMatchesException::class)
    fun handlePasswordNotMatchesException(ex: PasswordNotMatchesException): ResponseEntity<String> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(NotValidTransferException::class)
    fun handlePasswordNotMatchesException(ex: NotValidTransferException): ResponseEntity<String> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(NotValidTokenException::class)
    fun handleNotValidTokenException(ex: NotValidTokenException): ResponseEntity<String> {
        print("Exception occurred because of : ${ex.message}")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(AlreadyRegisteredException::class)
    fun handleAlreadyRegisteredException(ex: AlreadyRegisteredException): ResponseEntity<String> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(BankAccountNotExist::class)
    fun handleBankAccountNotExist(ex: BankAccountNotExist):ResponseEntity<String>{
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.message)
    }

    @ExceptionHandler(Exception::class)
    fun handlerException(ex: Exception):ResponseEntity<String>
    {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unhandled Exception occurred")
    }

}

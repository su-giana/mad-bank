package com.example.madbank.user_exception

class NotExistingUserException(message : String) : Exception(message) {
}

class PasswordNotMatchesException(message : String) : Exception(message)
{

}
package com.example.madbank.service

import com.example.madbank.model.User
import org.springframework.security.core.Authentication


interface UserService {
    public fun isIdAlreadyExist(id:String):Boolean

    public fun getUserById(id:Long):User

    public fun isUserAlreadyExist(id:Long): Boolean

    public fun isSocialIdAlreadyExist(socialId:String):Boolean

    public fun insertUser(user:User)

    public fun updateUser(user:User)

    public fun deleteUser(id:Long)

    public fun login(id:String, password:String):Authentication

    public fun getBalanceByuserId(id:Long):Long
}
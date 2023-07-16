package com.example.madbank.mapper

import com.example.madbank.model.User
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper {
    public fun getUserById(id:Long): User

    public fun getUserBySignUpId(id:String): User

    public fun getUserByNationalId(id:String): User

    public fun insertUser(user: User)

    public fun updateUser(user: User)

    public fun deleteUser(id:Long)

    public fun getBalanceByuserId(userId:Long):Long

}
package com.example.madbank.mapper

import org.apache.ibatis.annotations.Mapper

@Mapper
interface AccountMapper {
    public fun isAccountAlreadyExist(id:Long):Boolean

    public fun getAccountNumberByuserId(id: Long):Long
    public fun createAccount(userId:Long)
}
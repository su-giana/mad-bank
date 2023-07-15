package com.example.madbank.service

import com.example.madbank.model.Account

interface AccountService {

//    public fun getAccountNumberByuserId(id: Long):Long

    public fun isAccountAlreadyExist(userId:Long):Long
    public fun createAccount(userId: Long)
    public fun getAccountListByUid(id:Long):List<Account>
}
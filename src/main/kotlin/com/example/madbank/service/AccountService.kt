package com.example.madbank.service

import com.example.madbank.model.Account

interface AccountService {

//    public fun getAccountNumberByuserId(id: Long):Long

    public fun getUserIdByAccountId(id:Long):Long

    public fun isAccountAlreadyExist(userId:Long):Boolean
    public fun createAccount(userId: Long)
    public fun getAccountListByUid(id:Long):List<Account>
    public fun getBalanceByAccountId(id:Long):Long

    public fun getAccountIdByAccountNumber(account:String):Long

    public fun getAccountByAid(id:Long):Account

    public fun getUsernameByNumber(number:String):String
}
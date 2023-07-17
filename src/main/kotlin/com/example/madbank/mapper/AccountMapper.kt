package com.example.madbank.mapper

import com.example.madbank.model.Account
import org.apache.ibatis.annotations.Mapper

@Mapper
interface AccountMapper {
    public fun isAccountAlreadyExist(account_id:Long):Long //없으면 0 반환, 있으면 1 userId 반환.

    public fun getAccountNumberByuserId(userId: Long):Long
    public fun createAccount(userId:Long, accountNumber: String)

    public fun getLastAccountNumber():Long
    public fun getAccountByUid(id:Long):List<Account>

    public fun getBalanceByAccountId(account_id:Long):Long

    public fun getUserIdByAccountId(account_id:Long):Long
}
package com.example.madbank.mapper

import com.example.madbank.model.Account
import org.apache.ibatis.annotations.Mapper

@Mapper
interface TransactionMapper {
//    public fun getAccountByuserId(userId: Long): Account

//    public fun getAccountByaccountId(accountId:Long): Account


    public fun updateBalance(userId:Long, balance:Long)

    public fun changeResultcode(transactionId: Long)

}
package com.example.madbank.mapper

import com.example.madbank.model.Account
import com.example.madbank.model.Transaction
import org.apache.ibatis.annotations.Mapper

@Mapper
interface TransactionMapper {
//    public fun getAccountByuserId(userId: Long): Account

//    public fun getAccountByaccountId(accountId:Long): Account

    public fun xLockForTransfer(senderAccountId: Long, receiverAccountId: Long)

    public fun insertTransaction(item:Transaction)
    public fun updateBalance(userId:Long, balance:Long)

    public fun changeResultcode(transactionId: Long)

    public fun getAllTransactionWithAccountId(accountId:Long):List<Transaction>

}
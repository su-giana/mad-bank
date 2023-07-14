package com.example.madbank.service

import com.example.madbank.model.Transaction
import com.example.madbank.model.Account


interface TransactionService {


    public fun isBalanceEnough(id:Long, cost:Long): Boolean

    public fun deductSenderBalance(id: Long, cost: Long)

    public fun addReceiverBalance(id: Long, cost: Long)
    public fun withdrawal(userId:Long, cost:Long, transactionType: String)
    public fun deposit(userId:Long, cost:Long, transactionType: String)

    public fun admitTransfercode(transactionId:Long)
}
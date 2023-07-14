package com.example.madbank.service

import com.example.madbank.model.Account


interface TransactionService {


    public fun isBalanceEnough(id:Long, cost:Long): Boolean

    public fun deductSenderBalance(id: Long, cost: Long)

    public fun addReceiverBalance(id: Long, cost: Long)

    public fun admitTransfercode(userId:Long)
}
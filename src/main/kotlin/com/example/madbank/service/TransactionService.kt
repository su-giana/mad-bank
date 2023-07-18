package com.example.madbank.service

import com.example.madbank.model.Transaction
import com.example.madbank.model.Account


interface TransactionService {

    public fun isBalanceEnough(id:Long, cost:Long): Boolean

    public fun deductSenderBalance(senderAccountId: Long, cost: Long): Boolean

    public fun addReceiverBalance(id: Long, cost: Long): Boolean

    public fun insertTransaction(item:Transaction):Boolean

    public fun admitTransfercode(transactionId:Long)

    public fun getAllTransactionWithAccountId(accountId:Long):List<Transaction>
}
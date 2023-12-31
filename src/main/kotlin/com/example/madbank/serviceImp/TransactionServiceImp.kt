package com.example.madbank.serviceImp

import com.example.madbank.mapper.AccountMapper
import com.example.madbank.mapper.TransactionMapper
import com.example.madbank.mapper.UserMapper
import com.example.madbank.model.Transaction
import com.example.madbank.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional

@Service
class TransactionServiceImp:TransactionService {

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var transactionMapper: TransactionMapper

    @Autowired
    lateinit var accountMapper: AccountMapper

    override fun updateBalance(id:Long, balance:Long): Boolean {
        transactionMapper.updateBalance(id, balance)
        return true
    }

    override fun isBalanceEnough(id: Long, cost: Long): Boolean {
        val balance = accountMapper.getBalanceByAccountId(id)
        if (balance >= cost) {
            return true
        }
        return false
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = [Exception::class])
    override fun transferAtOnce(senderAccountId: Long, receiverAccountId: Long, cost: Long): Boolean {
        transactionMapper.xLockForTransfer(senderAccountId, receiverAccountId)
        var item: Transaction = Transaction(0, senderAccountId, receiverAccountId, "Transfer", cost, "Failed")
        transactionMapper.insertTransaction(item)

        var senderBalance = accountMapper.getBalanceByAccountId(senderAccountId)
        val senderResult = senderBalance - cost
        if(senderResult < 0) {
            throw Exception("Not enough balance")
        }
        transactionMapper.updateBalance(senderAccountId, senderResult)

        val receiverBalance = accountMapper.getBalanceByAccountId(receiverAccountId)
        val receiverResult = receiverBalance + cost
        transactionMapper.updateBalance(receiverAccountId, receiverResult)

        transactionMapper.changeResultcode(item.transactionId)
        return true
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = [Exception::class])
    override fun deductSenderBalance(senderAccountId: Long, cost: Long) : Boolean{
        // transfer인 경우에만 이용. sender의 balance에서 cost 만큼 차감한 값을 반환.
        var balance = accountMapper.getBalanceByAccountId(senderAccountId)  // select for update -> x lock

        var item = Transaction(0, senderAccountId, senderAccountId, "Withdrawal", cost, "Failed")
        transactionMapper.insertTransaction(item)

        val result = balance - cost
        if(result < 0) {
            throw Exception("Not enough balance")
        }
        transactionMapper.updateBalance(senderAccountId, result)

        transactionMapper.changeResultcode(item.transactionId)
        return true
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = [Exception::class])
    override fun addReceiverBalance(receiverAccountId: Long, cost: Long): Boolean {
        // transfer인 경우에만 이용. receiver의 balance에서 cost 만큼 더하여 업데이트함.
        val balance = accountMapper.getBalanceByAccountId(receiverAccountId)

        var item = Transaction(0, receiverAccountId, receiverAccountId, "Deposit", cost, "Failed")
        transactionMapper.insertTransaction(item)

        val result = balance + cost
        transactionMapper.updateBalance(receiverAccountId, result)

        transactionMapper.changeResultcode(item.transactionId)
        return true
    }

    override fun insertTransaction(item: Transaction):Boolean {
        try {
            transactionMapper.insertTransaction(item)
            var transactionId:Long = item.transactionId
            print(transactionId)

            return true
        }
        catch (e:Exception)
        {
            e.printStackTrace()
            return false
        }
    }

    override fun admitTransfercode(transactionId: Long) {
        //잘 되었다는 의미로 Failed에서 Success로 변경
        transactionMapper.changeResultcode(transactionId)
    }

    override fun getAllTransactionWithAccountId(accountId:Long):List<Transaction>
    {
        return transactionMapper.getAllTransactionWithAccountId(accountId)
    }
}
package com.example.madbank.serviceImp

import com.example.madbank.mapper.AccountMapper
import com.example.madbank.mapper.TransactionMapper
import com.example.madbank.mapper.UserMapper
import com.example.madbank.model.Transaction
import com.example.madbank.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TransactionServiceImp:TransactionService {

    @Autowired
    lateinit var userMapper: UserMapper

    @Autowired
    lateinit var transactionMapper: TransactionMapper

    @Autowired
    lateinit var accountMapper: AccountMapper
    override fun isBalanceEnough(id: Long, cost: Long): Boolean {
        val balance = accountMapper.getBalanceByAccountId(id)
        if (balance > cost) {
            return true
        }
        return false
    }

    override fun transferAtOnce(senderAccountId: Long, receiverAccountId: Long, cost: Long): Boolean {
        var senderBalance = accountMapper.getBalanceByAccountId(senderAccountId)
        val senderResult = senderBalance - cost
        transactionMapper.updateBalance(senderAccountId, senderResult)
        var senderCheck: Boolean = (senderResult == accountMapper.getBalanceByAccountId(senderAccountId))
        val receiverBalance = accountMapper.getBalanceByAccountId(receiverAccountId)
        val receiverResult = receiverBalance + cost
        transactionMapper.updateBalance(receiverAccountId, receiverResult)
        var receiverCheck: Boolean = (receiverResult == accountMapper.getBalanceByAccountId(receiverAccountId))
        if(senderCheck&&receiverCheck){
            return true // 이거 인식이 안 되는 거 같음...
        }
        return false
    }

    override fun deductSenderBalance(senderAccountId: Long, cost: Long) : Boolean{
        //transfer인 경우에만 이용. sender의 balance에서 cost 만큼 차감한 값을 반환.
        var balance = accountMapper.getBalanceByAccountId(senderAccountId)
        val result = balance - cost
        transactionMapper.updateBalance(senderAccountId, result)
        if(result == accountMapper.getBalanceByAccountId(senderAccountId)){
            return true
        }
        return false
    }

    override fun addReceiverBalance(receiverAccountId: Long, cost: Long): Boolean {
        //transfer인 경우에만 이용. receiver의 balance에서 cost 만큼 더하여 업데이트함.
        val balance = accountMapper.getBalanceByAccountId(receiverAccountId)
        val result = balance + cost
        transactionMapper.updateBalance(receiverAccountId, result)
        if(result == accountMapper.getBalanceByAccountId(receiverAccountId)){
            return true
        }
        return false
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


    override fun withdrawal(userId: Long, cost: Long): Boolean {
        val balance = userMapper.getBalanceByuserId(userId)
        val result = balance - cost
        transactionMapper.updateBalance(userId, result)
        return true //일단 무조건 성공한 걸로 하자
    }

    override fun deposit(userId: Long, cost: Long) : Boolean{
        val balance = userMapper.getBalanceByuserId(userId)
        val result = balance + cost
        transactionMapper.updateBalance(userId, result)
        return true //일단 무조건 성공한 걸로 하자
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
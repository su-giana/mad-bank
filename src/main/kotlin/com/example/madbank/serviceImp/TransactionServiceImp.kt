package com.example.madbank.serviceImp

import com.example.madbank.mapper.TransactionMapper
import com.example.madbank.mapper.UserMapper
import com.example.madbank.service.TransactionService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TransactionServiceImp:TransactionService {

    @Autowired
    lateinit var userMapper: UserMapper
    lateinit var transactionMapper: TransactionMapper
//    fun sendMoney(cost:Long)
//    {
//
//    }


//    override fun getBalanceById(id: Long): Long {
//        return userMapper.getBalanceById(id)
//    }

    override fun isBalanceEnough(id: Long, cost: Long): Boolean {
        val balance = userMapper.getBalanceById(id)
        if(balance>cost){
            return true
        }
        return false
    }

    override fun deductSenderBalance(id: Long, cost: Long){
        //transfer인 경우에만 이용. sender의 balance에서 cost 만큼 차감한 값을 반환.
        var balance = userMapper.getBalanceById(id)
        val result = balance-cost
        transactionMapper.updateBalance(id, result)
    }

    override fun addReceiverBalance(id: Long, cost: Long){
        //transfer인 경우에만 이용. receiver의 balance에서 cost 만큼 더하여 업데이트함.
        val balance = userMapper.getBalanceById(id)
        val result = balance+cost
        transactionMapper.updateBalance(id, result)
    }

    override fun admitTransfercode(userId: Long){
        //잘 되었다는 의미로 Failed에서 Success로 변경
        transactionMapper.changeResultcode(userId)
    }
}
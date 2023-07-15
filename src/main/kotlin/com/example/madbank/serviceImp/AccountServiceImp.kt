package com.example.madbank.serviceImp

import com.example.madbank.mapper.AccountMapper
import com.example.madbank.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountServiceImp:AccountService {

    @Autowired
    lateinit var accountMapper: AccountMapper

    override fun createAccount(userId: Long) {
        var newAccountNumber = accountMapper.getLastAccountNumber() + 1
        accountMapper.createAccount(userId, newAccountNumber)
    }
    override fun isAccountAlreadyExist(userId: Long): Long {
        return isAccountAlreadyExist(userId)
    }

}
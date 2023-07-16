package com.example.madbank.serviceImp

import com.example.madbank.mapper.AccountMapper
import com.example.madbank.model.Account
import com.example.madbank.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AccountServiceImp:AccountService {

    @Autowired
    lateinit var accountMapper: AccountMapper

    override fun createAccount(userId: Long) {
        var newAccountNumber:Long = accountMapper.getLastAccountNumber() + 1
        accountMapper.createAccount(userId, newAccountNumber)
    }

    override fun getAccountListByUid(id: Long):List<Account> {
        return accountMapper.getAccountByUid(id)
    }

    override fun getBalanceByAccountId(account_id: Long): Long {
        return accountMapper.getBalanceByAccountId(account_id)
    }

    override fun getUserIdByAccountId(account_id: Long): Long {
        return accountMapper.getUserIdByAccountId(account_id)
    }

    override fun isAccountAlreadyExist(account_id: Long): Boolean {
        if(accountMapper.isAccountAlreadyExist(account_id)==null) return false
        return true
    }

}
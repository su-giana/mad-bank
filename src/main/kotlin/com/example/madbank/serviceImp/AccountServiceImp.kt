package com.example.madbank.serviceImp

import com.example.madbank.mapper.AccountMapper
import com.example.madbank.model.Account
import com.example.madbank.service.AccountService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class AccountServiceImp:AccountService {

    @Autowired
    lateinit var accountMapper: AccountMapper

    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = [Exception::class])
    override fun createAccount(userId: Long) {
        lateinit var newAccountNumber: String
        do {
            newAccountNumber = "7282618" + Random.nextInt(100000, 1000000);
        } while(accountMapper.isAccountNumberAlreadyExist(newAccountNumber) != null)

        accountMapper.createAccount(userId, newAccountNumber)
    }

    override fun getAccountListByUid(id: Long):List<Account> {
        return accountMapper.getAccountByUid(id)
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    override fun getBalanceByAccountId(account_id: Long): Long {
        return accountMapper.getBalanceByAccountId(account_id)
    }

    override fun getAccountIdByAccountNumber(account: String): Long {
        return accountMapper.getAccountIdByAccountNumber(account)
    }

    override fun getAccountByAid(id: Long): Account {
        return accountMapper.getAccountByAid(id)
    }

    override fun getUserIdByAccountId(account_id: Long): Long {
        return accountMapper.getUserIdByAccountId(account_id)
    }

    override fun isAccountAlreadyExist(account_id: Long): Boolean {
        if(accountMapper.isAccountAlreadyExist(account_id)==null) return false
        return true
    }

}
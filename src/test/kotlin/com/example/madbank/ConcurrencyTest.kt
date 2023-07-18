package com.example.madbank

import com.example.madbank.service.AccountService
import com.example.madbank.service.TransactionService
import junit.framework.TestCase.assertEquals
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@RunWith(SpringRunner::class)
@SpringBootTest
class ConcurrencyTest {
    var service: ExecutorService = Executors.newFixedThreadPool(100)

    @Autowired lateinit var accountService: AccountService
    @Autowired lateinit var transactionService: TransactionService

    var iter: Int = 5000
    var cost: Long = 100

    @BeforeEach
    fun setUp() {
        println("SETUP!!")
        transactionService.updateBalance(1, 5000000)
        transactionService.updateBalance(2, 5000000)
    }

    @Test
    fun addReceiverBalanceTest() {
        val beforeSum1 = accountService.getBalanceByAccountId(1)
        val beforeSum2 = accountService.getBalanceByAccountId(2)

        val latch = CountDownLatch(iter)
        for(i in 1..iter) {
            service.execute {
                transactionService.addReceiverBalance(1, cost)
                transactionService.addReceiverBalance(2, cost)
                latch.countDown()
            }
        }
        latch.await()

        val afterSum1 = accountService.getBalanceByAccountId(1)
        val afterSum2 = accountService.getBalanceByAccountId(2)
        assertEquals(iter*cost, afterSum1-beforeSum1)
        assertEquals(iter*cost, afterSum2-beforeSum2)
    }

    @Test
    fun deductSenderBalanceTest() {
        val beforeSum1 = accountService.getBalanceByAccountId(1)
        val beforeSum2 = accountService.getBalanceByAccountId(2)

        val latch = CountDownLatch(iter)
        for(i in 1..iter) {
            service.execute {
                transactionService.deductSenderBalance(1, cost)
                transactionService.deductSenderBalance(2, cost)
                latch.countDown()
            }
        }
        latch.await()

        val afterSum1 = accountService.getBalanceByAccountId(1)
        val afterSum2 = accountService.getBalanceByAccountId(2)
        print("beforeSum1: $beforeSum1, afterSum1: $afterSum1\n")
        assertEquals(-iter*cost, afterSum1-beforeSum1)
        assertEquals(-iter*cost, afterSum2-beforeSum2)
    }

    @Test
    fun transferMoneyTest() {
        val beforeSum1 = accountService.getBalanceByAccountId(1)
        val beforeSum2 = accountService.getBalanceByAccountId(2)

        val latch = CountDownLatch(iter)
        for(i in 1..iter) {
            service.execute {
                transactionService.transferAtOnce(1, 2, cost)
                transactionService.transferAtOnce(2, 1, cost)
                latch.countDown()
            }
        }
        latch.await()

        val afterSum1 = accountService.getBalanceByAccountId(1)
        val afterSum2 = accountService.getBalanceByAccountId(2)

        assertEquals(0, afterSum1-beforeSum1)
        assertEquals(0, afterSum2-beforeSum2)
    }
}
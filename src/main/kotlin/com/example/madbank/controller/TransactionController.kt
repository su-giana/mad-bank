package com.example.madbank.controller

import com.example.madbank.model.Transaction
import com.example.madbank.model.TransferForm
import com.example.madbank.model.User
import com.example.madbank.security.JwtTokenUtil
import com.example.madbank.service.AccountService
import com.example.madbank.service.TransactionService
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.NotValidTokenException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@CrossOrigin(allowedHeaders = ["*"])
@Controller
class TransactionController {
    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @PostMapping("/transfer_money")
    public fun transferMoney(
            @RequestBody transferForm: TransferForm
    ): ResponseEntity<String>
    {
        val transactionType:String = transferForm.transactionType
        val senderAccountId:Long = transferForm.senderAccountId
        val receiverAccountNumber:String = transferForm.receiverAccountNumber
        val cost = transferForm.cost

        var receiverAccountId:Long = accountService.getAccountIdByAccountNumber(receiverAccountNumber)

        if(!(accountService.isAccountAlreadyExist(senderAccountId) &&
                    accountService.isAccountAlreadyExist(receiverAccountId))) {
            return ResponseEntity.badRequest().body("User doesn't exist in DB haha from.\uD83D\uDC7B Junseo")
        }
        if(transactionType != "Transfer") {
            return ResponseEntity.badRequest().body("Transaction type is not Transfer")
        }

        try {
            transactionService.transferAtOnce(senderAccountId, receiverAccountId, cost)
        } catch(e:Exception) {
            // rollback occured
            println("rollback occured")
            return ResponseEntity.internalServerError().body("rollback occured! FAILED: transfer money failed")
        }

        return ResponseEntity.ok("SUCCEED")
    }

    @PostMapping("/my_transfer")
    public fun myTransfer(
            @RequestBody transferForm: TransferForm
    ): ResponseEntity<String>
    {
        val transactionType:String = transferForm.transactionType
        val senderAccountId:Long = transferForm.senderAccountId
        val receiverAccountNumber:String = transferForm.receiverAccountNumber
        val cost = transferForm.cost

        var receiverAccountId:Long = accountService.getAccountIdByAccountNumber(receiverAccountNumber)

        try {
            if(senderAccountId==receiverAccountId){
                if (accountService.isAccountAlreadyExist(senderAccountId)) {
                    if (transactionType == "Deposit") {
                        var item:Transaction = Transaction(0, senderAccountId, receiverAccountId, transactionType, cost, "Failed")
                        transactionService.insertTransaction(item)

                        val recover = transactionService.addReceiverBalance(senderAccountId, cost)
                        var transactionId:Long = item.transactionId
                        transactionService.admitTransfercode(transactionId)
                        return ResponseEntity.ok("SUCCEED")
                    }else if((transactionType =="Withdrawal")&&transactionService.isBalanceEnough(senderAccountId, cost)){
                        var item:Transaction = Transaction(0, senderAccountId, receiverAccountId, transactionType, cost, "Failed")
                        transactionService.insertTransaction(item)

                        val recover = transactionService.deductSenderBalance(senderAccountId, cost)
                        var transactionId = item.transactionId
                        transactionService.admitTransfercode(transactionId)
                        return ResponseEntity.ok("SUCCEED")
                    }
                    return ResponseEntity.badRequest().body("FAILED: No Enough Money or Not proper type")
                }
            }
            return ResponseEntity.badRequest().body("User doesn't exist in DB haha from.\uD83D\uDC7B Jiyeon")

        }catch (e:Exception)
        {
            return ResponseEntity.badRequest().body("Cannot transfer money")
        }
    }

    @GetMapping("/consume_list")
    public fun getTransactionList(@RequestHeader("Authorization") token:String, @RequestParam(value = "account", required = true) account:Long):ResponseEntity<String>
    {
        if(!jwtTokenUtil.validateToken(token.substring(7))) throw NotValidTokenException("not valid token, cannot access to transaction list")

        var transactions: List<Transaction> = transactionService.getAllTransactionWithAccountId(account)
        var json:String = jacksonObjectMapper().writeValueAsString(transactions)
        return ResponseEntity.ok(json)
    }

}
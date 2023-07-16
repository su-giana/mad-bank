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

@Controller
class TransactionController {
    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @PostMapping("/transfer_money")
    public fun transferMoney(
            @RequestBody transferForm: TransferForm
    ): ResponseEntity<String>
    {
        val transactionId:Long = transferForm.transactionId
        val transactionType:String = transferForm.transactionType
        val senderAccountId:Long = transferForm.senderAccountId
        val receiverAccountId:Long = transferForm.receiverAccountId
        val cost = transferForm.cost

        try {
            if(accountService.isAccountAlreadyExist(senderAccountId)&&accountService.isAccountAlreadyExist(receiverAccountId)){
                if(transactionService.isBalanceEnough(senderAccountId, cost) && (transactionType == "Transfer")){
                    val senderResult = transactionService.deductSenderBalance(senderAccountId, cost)
                    val receiverResult = transactionService.addReceiverBalance(receiverAccountId, cost)
                    // transactionService.admitTransfercode(transactionType)
                    if(senderResult && receiverResult){ // 둘다 잘 됨.
                        transactionService.admitTransfercode(transactionId)
                        return ResponseEntity.ok("SUCCEED")
                    }else if(senderResult){  //송금자 돈이 빠져나가기만 한 경우
                        val recover = transactionService.deposit(senderAccountId, cost)//근데 이게 실패하면 어떡해 ? ㅋㅋㅋㅋ
                        //만약 recover 도 잘 되었는지 테스트 하려면 아래 코드를 쓰면 됨. - 참고로 아래 receiverResult에도 추가해줘야 함.
//                        if(recover){
//                            return ResponseEntity.ok("sender Result FAILED but Recovered before state")
//                        }else{
//                            return ResponseEntity.ok("sender Result FAILED and Can't Recovered before state")
//                        }
                        return ResponseEntity.ok("sender Result FAILED")
                    }else if(receiverResult){
                        val recover = transactionService.withdrawal(receiverAccountId, cost)
                        return ResponseEntity.ok("receiver Result FAILED")
                    }
                }
                return ResponseEntity.ok("FAILED: No Enough Money or Not Transfer type")
            }
            return ResponseEntity.badRequest().body("User doesn't exist in DB haha from.\uD83D\uDC7B Jiyeon")

        }catch (e:Exception)
        {
            return ResponseEntity.badRequest().body("Cannot transfer money")
        }
    }

    @PostMapping("/my_transfer")
    public fun myTransfer(
            @RequestBody transferForm: TransferForm
    ): ResponseEntity<String>
    {
        val transactionId:Long = transferForm.transactionId
        val transactionType:String = transferForm.transactionType
        val senderAccountId:Long = transferForm.senderAccountId
        val receiverAccountId:Long = transferForm.receiverAccountId
        val cost = transferForm.cost

        try {
            if(senderAccountId==receiverAccountId){
                if (accountService.isAccountAlreadyExist(senderAccountId)) {
                    if (transactionType == "Transfer") {
                        val recover = transactionService.deposit(senderAccountId, cost)
                        transactionService.admitTransfercode(transactionId)
                        return ResponseEntity.ok("SUCCEED")
                    }else if((transactionType =="Withdrawal")&&transactionService.isBalanceEnough(senderAccountId, cost)){
                        val recover = transactionService.withdrawal(senderAccountId, cost)
                        transactionService.admitTransfercode(transactionId)
                        return ResponseEntity.ok("SUCCEED")
                    }
                    return ResponseEntity.ok("FAILED: No Enough Money or Not proper type")
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
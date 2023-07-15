package com.example.madbank.controller

import com.example.madbank.model.TransferForm
import com.example.madbank.model.User
import com.example.madbank.security.JwtTokenUtil
import com.example.madbank.service.TransactionService
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.NotValidTokenException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class TransactionController {
    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    @GetMapping("/transfer_money")
    public fun transferMoney(
            @RequestBody transferForm: TransferForm
    ): ResponseEntity<String>
    {
        //1. senderId와 receiverId가 모두 DB에 존재하는지 확인한다. => userService.isUserAlreadyExist
        //2. senderId로 getBalance해서, cost보다 getBalance가 많은지 확인한다. (잔액>송금액) =>userService.isBalanceEnough
        //3, senderId의 금액 차감 =>deductSenderBalance, UpdateUser
        //4. receiverId의 금액 추가 =>addReceiverBalance, UpdateUser
        //5. resultCode를 'Success'로 설정한다.

        val transactionType:Long = transferForm.transactionType
        val senderId:Long = transferForm.senderId
        val receiverId:Long = transferForm.receiverId
        val cost = transferForm.cost

        try {

            if(userService.isUserAlreadyExist(senderId)&&userService.isUserAlreadyExist(receiverId)){
                if(transactionService.isBalanceEnough(senderId, cost)){
                    transactionService.deductSenderBalance(senderId, cost)
                    transactionService.addReceiverBalance(receiverId, cost)
                    transactionService.admitTransfercode(transactionType)
                    return ResponseEntity.ok("SUCCEED")
                }else{
                    return ResponseEntity.ok("FAILED")
                }
            }else{
                return ResponseEntity.badRequest().body("User doesn't exist in DB haha from.\uD83D\uDC7B Jiyeon")
            }
        }catch (e:Exception)
        {
            return ResponseEntity.badRequest().body("Cannot transfer money")
        }
    }

}
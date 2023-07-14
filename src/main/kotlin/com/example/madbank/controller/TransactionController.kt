package com.example.madbank.controller

import com.example.madbank.model.User
import com.example.madbank.service.TransactionService
import com.example.madbank.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TransactionController {
    @Autowired
    lateinit var transactionService: TransactionService

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/transfer_money")
    public fun transferMoney(
            @RequestParam(value="transactionId", required=true) transactionId:Long,
            @RequestParam(value="senderId", required = true) senderId:Long,
            @RequestParam(value="receiverId", required = true) receiverId:Long,
            @RequestParam(value="transactionType", required = true) transactionType:String,
            @RequestParam(value="cost", required = true) cost:Long,
    ): ResponseEntity<String>
    {
        //1. senderId와 receiverId가 모두 DB에 존재하는지 확인한다. => userService.isUserAlreadyExist
        //2. senderId로 getBalance해서, cost보다 getBalance가 많은지 확인한다. (잔액>송금액) =>userService.isBalanceEnough
        //3, senderId의 금액 차감 =>deductSenderBalance, UpdateUser
        //4. receiverId의 금액 추가 =>addReceiverBalance, UpdateUser
        //5. resultCode를 'Success'로 설정한다.
        try {
            if(userService.isUserAlreadyExist(senderId)&&userService.isUserAlreadyExist(receiverId)){
                if(transactionService.isBalanceEnough(senderId, cost)){
                    transactionService.deductSenderBalance(senderId, cost)
                    transactionService.addReceiverBalance(receiverId, cost)
                    transactionService.admitTransfercode(transactionId)
                    return ResponseEntity.ok("$transactionId transfer success. happyhappyhappy")
                }else{
                    return ResponseEntity.ok("Error Occur: Sender doesn't have enough money to transfer haha.\uD83D\uDC7B from JY")
                }
            }else{
                return ResponseEntity.ok("User doesn't exist in DB haha from.\uD83D\uDC7B Jiyeon")
            }
        }catch (e:Exception)
        {
            throw e
        }
    }

    @GetMapping("/get_balance")
    public fun getBalance(@RequestParam(value = "id", required=true) id:Long): ResponseEntity<String>
    {
        try{  //만약 없는 유저의 아이디를 검색하면 일단 오류가 남. ㅜㅜ
            if(userService.isUserAlreadyExist(id)){
                var balance:Long = userService.getBalanceByuserId(id)
                var value:String = balance.toString()
                return ResponseEntity.ok(value) // string -> html.//header body로 날리려면 http 통신을 위해서는  무조건 response 써야 함.
            }else{
                return ResponseEntity.ok("error occured from JIYEON haha")
            }

        }catch (e:Exception)
        {
            throw e
        }
    }
}
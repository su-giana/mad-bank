package com.example.madbank.controller

import com.example.madbank.model.User
import com.example.madbank.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class TransactionController {
    @Autowired
    lateinit var transactionService:TransactionService

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/get_money")
    public fun getMoney(@RequestParam(value="cost", required = true) cost:Long)
    {
        try {
            var user: User = userService.getUserById(1)
//            var balance = user.
            transactionService.insertMoney(cost)
        }
    }

    @GetMapping("/get_balance")
    public fun getBalance(@RequestParam(value = "id", required=true) id:Long): ResponseEntity<String>
    {
        try{  //만약 없는 유저의 아이디를 검색하면 일단 오류가 남. ㅜㅜ
            if(userService.isUserAlreadyExist(id))    {

                var balance:Long = userService.getBalanceById(id)
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
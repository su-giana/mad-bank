//package com.example.madbank.controller
//
//import com.example.madbank.model.User
//import com.example.madbank.service.UserService
//import org.springframework.beans.factory.annotation.Autowired
//import org.springframework.stereotype.Controller
//import org.springframework.web.bind.annotation.GetMapping
//import org.springframework.web.bind.annotation.RequestParam
//
//@Controller
//class TransactionController {
//    @Autowired
//    lateinit var transactionService:TransactionService
//
//    @Autowired
//    lateinit var userService: UserService
//
//    @GetMapping("/get_money")
//    public fun getMoney(@RequestParam(value="cost", required = true) cost:Long)
//    {
//        try {
//            var user: User = userService.getUserById(1)
//            var balance = user.
//            transactionService.insertMoney(cost)
//        }
//    }
//}
package com.example.madbank.controller

import org.springframework.beans.factory.annotation.Autowired
import com.example.madbank.service.AccountService
import com.example.madbank.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class AccountController {
    @Autowired
    lateinit var accountService: AccountService
//
    @Autowired
    lateinit var userService: UserService

    @GetMapping("/create_account")
    public fun createAccount(
            @RequestParam(value="userId", required = true) userId:Long
    ): ResponseEntity<String> {
        try{
            if(userService.isUserAlreadyExist(userId)){
                accountService.createAccount(userId)
                return ResponseEntity.ok("Create $userId's Account Success") // string -> html.//header body로 날리려면 http 통신을 위해서는  무조건 response 써야 함.
            }else{
                return ResponseEntity.ok("error occured from JIYEON haha")
            }
        }catch(e:Exception)
        {
            throw e
        }
    }

}
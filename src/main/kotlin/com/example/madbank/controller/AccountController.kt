package com.example.madbank.controller

import com.example.madbank.model.Account
import com.example.madbank.security.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import com.example.madbank.service.AccountService
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.NotExistingUserException
import com.example.madbank.user_exception.NotValidTokenException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@CrossOrigin(allowedHeaders = ["*"])
@Controller
class AccountController {
    @Autowired
    lateinit var accountService: AccountService
//
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    var objectMapper:ObjectMapper = ObjectMapper()

    @GetMapping("/get_username_with_aid")
    public fun getUsernameWithAid(@RequestHeader("Authorization")token: String, @RequestParam(value = "number", required = true) number:String):ResponseEntity<String>
    {
        var username:String =  accountService.getUsernameByNumber(number)
        return ResponseEntity.ok(objectMapper.writeValueAsString(username))
    }

    @GetMapping("/create_account")
    public fun createAccount(
            @RequestHeader("Authorization") token:String
    ): ResponseEntity<String> {
            if(!jwtTokenUtil.validateToken(token.substring(7))) throw NotValidTokenException("Not valid Token, could not create account")

            var userId:Long = jwtTokenUtil.extractUserId(token.substring(7))

            if(userService.isUserAlreadyExist(userId)){
                accountService.createAccount(userId)
                return ResponseEntity.ok("Create $userId's Account Success") // string -> html.//header body로 날리려면 http 통신을 위해서는  무조건 response 써야 함.
            }else{
                throw NotExistingUserException("User not exist, cannot create new account")
            }
    }

    @GetMapping("/account_list")
    public fun getAccountList(@RequestHeader("Authorization") token:String):ResponseEntity<String>
    {
        try {
            if(!jwtTokenUtil.validateToken(token.substring(7)))  throw NotValidTokenException("token is not valid, cannot get account list")

            var id:Long = jwtTokenUtil.extractUserId(token.substring(7))

            var accounts:List<Account> = accountService.getAccountListByUid(id)
            var json:String = objectMapper.writeValueAsString(accounts)
            return ResponseEntity.ok(json)
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("/get_balance")
    public fun getBalance(@RequestParam(value = "account_id", required = true) accountId:Long): ResponseEntity<String>
    {
        try{  //만약 없는 유저의 아이디를 검색하면 일단 오류가 남. ㅜㅜ
            if(accountService.isAccountAlreadyExist(accountId)){
                val balance: Long = accountService.getBalanceByAccountId(accountId)
                return ResponseEntity.ok(balance.toString())
            }
            return ResponseEntity.badRequest().body("user account doesn't exist")
//            var balance:Long = accountService.getBalanceByAccountId(id).balance
//            var value:String = balance.toString()
//            return ResponseEntity.ok(value) // string -> html.//header body로 날리려면 http 통신을 위해서는  무조건 response 써야 함.

        }catch (e:Exception)
        {
            return ResponseEntity.badRequest().body("cannot load balance")
        }
    }
}
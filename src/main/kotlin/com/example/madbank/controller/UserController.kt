package com.example.madbank.controller

import com.example.madbank.model.*
import com.example.madbank.security.JwtTokenUtil
import com.example.madbank.service.AccountService
import com.example.madbank.service.MarketService
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.AlreadyRegisteredException
import com.example.madbank.user_exception.NotValidTokenException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.coyote.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate

@CrossOrigin(allowedHeaders = ["*"])
@Controller
class UserController {

    @Autowired
    lateinit var userService:UserService

    @Autowired
    lateinit var jwtTokenUtil:JwtTokenUtil

    @Autowired
    lateinit var accountService: AccountService

    @Autowired
    lateinit var marketService: MarketService

    var objectMapper:ObjectMapper = ObjectMapper()

    @GetMapping("/find_id")
    public fun findId(@RequestParam(value="id", required = true) id:String ):ResponseEntity<String>
    {
            if(userService.isIdAlreadyExist(id))    return ResponseEntity.ok("NONVALID")
            else        return ResponseEntity.ok("VALID")
    }

    @GetMapping("form")
    public fun formPage():String
    {
        return "form"
    }

    @GetMapping("login")
    public fun loginPage(model:Model, @RequestParam(value = "account", required = true) aid:Long, @RequestParam(value = "product", required = true) pid:Long):String
    {
        model.addAttribute("accountId", aid)
        model.addAttribute("productId", pid)

        return "login"
    }

    @GetMapping("get_username_with_id")
    public fun selfName(@RequestHeader("Authorization") token:String, @RequestParam(value = "id", required = false) id:Long?):ResponseEntity<String>
    {
        if(!jwtTokenUtil.validateToken(token.substring(7))) throw NotValidTokenException("token not validate, cannot refer username");

        if(id == null || id.toInt() == 0)
        {
            var id:Long = jwtTokenUtil.extractUserId(token.substring(7))
            var user:User = userService.getUserById(id)
            var username:String = user.name

            return ResponseEntity.ok(username)
        }
        else
        {
            var user:User = userService.getUserById(accountService.getUserIdByAccountId(id))
            var username:String = user.name
            var json:String = objectMapper.writeValueAsString(username)
            return ResponseEntity.ok(json)
        }
    }

    @PostMapping("/auth")
    public fun loginLogin(@RequestBody loginForm:LoginForm): ResponseEntity<String>
    {
            val id:String = loginForm.id
            val password:String = loginForm.password

            var token:Authentication = userService.login(id, password)
            var body:String = jwtTokenUtil.generateAccessToken(token)

            return ResponseEntity.ok(body)
    }

    @PostMapping("/pay_login")
    public fun payLogin(payLoginForm: PayLoginForm, model:Model):String
    {
        try {
            val id:String = payLoginForm.id
            val password:String = payLoginForm.password
            val pid:Long = payLoginForm.pid
            val aid:Long = payLoginForm.aid

            var token:Authentication = userService.login(id, password)
            var body:String = jwtTokenUtil.generateAccessToken(token)

            var product:Market = marketService.getProductById(pid);
            var account:Account = accountService.getAccountByAid(aid);

            if(product == null || account==null)    throw NotValidTokenException("product or account does not exist")

            model.addAttribute("jwtToken", body)
            model.addAttribute("product", product)
            model.addAttribute("account", account)
            model.addAttribute("date", LocalDate.now().toString())

            return "form"
        }
        catch (e:Exception)
        {
            return "redirect:http://127.0.0.1:8080/transaction_done?transactionType=400"
        }
    }

    @GetMapping("/signup")
    public fun signUpPage():String
    {
        print("reload")
        return "signup"
    }

    @PostMapping("/signup")
    public fun signUpLogin(@RequestBody user: User):ResponseEntity<String>
    {
        if(userService.isSocialIdAlreadyExist(user.nationalId)) throw AlreadyRegisteredException("You are already registered")
        userService.insertUser(user)
        return ResponseEntity.ok("successfully enrolled")
    }


}
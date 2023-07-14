package com.example.madbank.controller

import com.example.madbank.model.LoginForm
import com.example.madbank.model.User
import com.example.madbank.security.JwtTokenUtil
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.AlreadyRegisteredException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Controller
class UserController {

    @Autowired
    lateinit var userService:UserService

    @Autowired
    lateinit var jwtTokenUtil:JwtTokenUtil

    @GetMapping("/find_id")
    public fun findId(@RequestParam(value="id", required = true) id:String ):ResponseEntity<String>
    {
        try {
            if(userService.isIdAlreadyExist(id))    return ResponseEntity.ok("NONVALID")
            else        return ResponseEntity.ok("VALID")
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

    @GetMapping("login")
    public fun loginPage():String
    {
        return "login"
    }

    @PostMapping("/auth")
    public fun loginLogin(@RequestBody loginForm:LoginForm): ResponseEntity<String>
    {
        try {
            val id:String = loginForm.id
            val password:String = loginForm.password

            print(id + " " + password)


            var token:Authentication = userService.login(id, password)
            var body:String = jwtTokenUtil.generateAccessToken(token)

            return ResponseEntity.ok(body)
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
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
        print("bye")
        try {
            if(userService.isSocialIdAlreadyExist(user.nationalId)) throw AlreadyRegisteredException("You are already registered")
            userService.insertUser(user)
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
        }
        return ResponseEntity.ok("successfully enrolled")
    }


}
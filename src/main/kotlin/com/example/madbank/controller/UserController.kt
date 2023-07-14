package com.example.madbank.controller

import com.example.madbank.service.UserService
import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class UserController {

    @Autowired
    lateinit var userService:UserService

    @GetMapping("/find_id")
    public fun findId(@RequestParam(value="id", required = true) id:String ):ResponseEntity<String>
    {
        try {
            if(userService.isIdAlreadyExist(id))    return ResponseEntity.ok("NONVALID")
            else        return ResponseEntity.ok("VALID")
        }
        catch (e:Exception)
        {
            throw e
        }
    }

    @PostMapping("/auth")
    public fun loginLogin(@RequestParam(value = "id", required = true) id:String, @RequestParam(value = "password", required = true) password:String): ResponseEntity<String>
    {
        try {
            var token: org.springframework.security.core.Authentication = userService.login(id, password)
            var body:String = jwtTokenUtil.generateToken(token)

            return ResponseEntity.ok(body)
        }
        catch (e:Exception)
        {
            throw e
        }
    }




}
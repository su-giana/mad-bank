package com.example.madbank.controller

import com.example.madbank.security.JwtTokenUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@CrossOrigin(allowedHeaders = ["*"])
@Controller
class SecurityController {

    @Autowired
    lateinit var jwtTokenUtil:JwtTokenUtil

    @GetMapping("/is_expired")
    public fun isExpired(@RequestHeader("Authorization") token:String)
    {
        if(jwtTokenUtil.isExpired(token.substring(7)))
        {
            return
        }
    }
}
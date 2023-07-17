package com.example.madbank.controller

import com.example.madbank.model.Account
import com.example.madbank.model.Market
import com.example.madbank.security.JwtTokenUtil
import com.example.madbank.service.AccountService
import com.example.madbank.service.MarketService
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.NotExistingUserException
import com.example.madbank.user_exception.NotValidTokenException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@CrossOrigin(allowedHeaders = ["*"])
@Controller
class MarketController {

    @Autowired
    lateinit var accountService: AccountService
    //
    @Autowired
    lateinit var userService: UserService

    @Autowired
    lateinit var marketService: MarketService

    @Autowired
    lateinit var jwtTokenUtil: JwtTokenUtil

    var objectMapper: ObjectMapper = ObjectMapper()

    @GetMapping("/market/products")
    public fun getProductList(@RequestHeader("Authorization") token:String):ResponseEntity<String>
    {
        try {
            if(!jwtTokenUtil.validateToken(token.substring(7)))  throw NotValidTokenException("token is not valid, cannot get account list")

//            var id:Long = jwtTokenUtil.extractUserId(token.substring(7))
//
            var products:List<Market> = marketService.getProductList()
            var json:String = objectMapper.writeValueAsString(products)
            print(json)
            return ResponseEntity.ok(json)
        }
        catch (e:Exception)
        {
            return ResponseEntity.badRequest().body(e.message)
        }
    }

}
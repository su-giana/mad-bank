package com.example.madbank.security

import com.example.madbank.user_exception.NotValidToken
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import lombok.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct
import javax.crypto.spec.SecretKeySpec
import kotlin.properties.Delegates
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

@Component
class JwtTokenUtil {
    @org.springframework.beans.factory.annotation.Value("\${app.jwtSecret}")
    private lateinit var jwtSecret:String

    @org.springframework.beans.factory.annotation.Value("\${app.jwtExpirationMs}")
    private lateinit var jwtExpirationMs : String

    private lateinit var key:Key

    @PostConstruct
    public fun init()
    {
        var decodeKey:ByteArray = Base64.getDecoder().decode(jwtSecret)
        this.key = SecretKeySpec(decodeKey, "HmacSHA512")
    }

    public fun validateToken(token:String):Boolean
    {
        try {
            var claimsJws: Jws<Claims> = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.toByteArray()))
                    .build()
                    .parseClaimsJws(token)

            return true
        }
        catch (e:Exception)
        {
            return false
        }
    }

    public fun isExpired(token:String, secretKey:String): Boolean
    {
        return Jwts.parser().setSigningKey(secretKey.toByteArray()).parseClaimsJws(token)
                .body.expiration.before(Date())
    }

    public fun generateToken(authentication: Authentication):String
    {
        var now:Date = Date()
        var expiry:Date = Date(now.time + jwtExpirationMs.toInt())

        var principal = authentication.principal

        if(principal == null)   throw InvalidTo
    }

}
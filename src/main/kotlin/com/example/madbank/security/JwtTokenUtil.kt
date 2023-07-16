package com.example.madbank.security

import com.example.madbank.user_exception.NotValidTokenException
import io.jsonwebtoken.*
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

    @org.springframework.beans.factory.annotation.Value("\${app.jwtExpirationsMs}")
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

    public fun isExpired(token:String): Boolean
    {
        return Jwts.parser().setSigningKey(jwtSecret.toByteArray()).parseClaimsJws(token)
                .body.expiration.before(Date())
    }

    public fun generateAccessToken(authentication: Authentication):String
    {
        var now:Date = Date()
        var expiry:Date = Date(now.time + jwtExpirationMs.toInt())

        var principal:String = authentication.principal.toString()

        if(principal == null || principal == "")
            throw NotValidTokenException("cannot generate your token, your authentication is broken")

        return Jwts.builder()
                .setSubject(principal)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS512, jwtSecret.toByteArray())
                .compact()
    }

    public fun extractUserId(token:String): Long
    {
        var principal:String = Optional.ofNullable(token)
                .map{ t -> Jwts.parser().setSigningKey(jwtSecret.toByteArray()).parseClaimsJws(t).body.subject }
                .orElseThrow { NotValidTokenException("cannot extract userId, your authentication is broken") }

        return principal.toLong()
    }

}
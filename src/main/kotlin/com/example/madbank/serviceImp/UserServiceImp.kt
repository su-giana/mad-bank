package com.example.madbank.serviceImp

import com.example.madbank.mapper.UserMapper
import com.example.madbank.model.User
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.NotExistingUserException
import com.example.madbank.user_exception.PasswordNotMatchesException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImp :UserService{
    @Autowired
    lateinit var userMapper: UserMapper

    private var passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

    override fun isIdAlreadyExist(id: String): Boolean {
        if(userMapper.getUserBySignUpId(id) == null)    return false
        return true
    }

    override fun getUserById(id: Long):User {
        return userMapper.getUserById(id)
    }

    override fun isUserAlreadyExist(id: Long): Boolean {
        if(userMapper.getUserById(id) == null)  return false
        return true
    }

    override fun isSocialIdAlreadyExist(socialId: String): Boolean {
        if(userMapper.getUserByNationalId(socialId) == null)    return false
        return true
    }

    override fun insertUser(user: User) {
        try {
            userMapper.insertUser(user)
        }
        catch (e:Exception)
        {
            throw e
        }
    }

    override fun updateUser(user: User) {
        try {
            userMapper.updateUser(user)
        }
        catch(e:Exception)
        {
            throw e
        }
    }

    override fun deleteUser(id:Long) {
        try {
            userMapper.deleteUser(id)
        }
        catch (e:Exception)
        {
            throw e
        }
    }

    override fun login(id: String, password: String):Authentication {
        var token:UsernamePasswordAuthenticationToken
        var user:User = userMapper.getUserBySignUpId(id)

        if(user==null)
        {
            // no such user
            throw NotExistingUserException("User not existing")
        }
        else if(passwordEncoder.matches(password, user.password))
        {
            // valid password and id
            var roles:List<GrantedAuthority> = ArrayList()
            roles += SimpleGrantedAuthority("USER")

            token = UsernamePasswordAuthenticationToken(user.id, null, roles)

            return token
        }
        // password not match
        throw PasswordNotMatchesException("Your password is wrong")
    }

    override fun getBalanceByuserId(id: Long): Long {
        return userMapper.getBalanceByuserId(id)
    }

}
package com.example.madbank.serviceImp

import com.example.madbank.mapper.AccountMapper
import com.example.madbank.mapper.UserMapper
import com.example.madbank.model.User
import com.example.madbank.service.UserService
import com.example.madbank.user_exception.BankAccountNotExist
import com.example.madbank.user_exception.NotExistingUserException
import com.example.madbank.user_exception.NotValidTokenException
import com.example.madbank.user_exception.PasswordNotMatchesException
import net.bytebuddy.pool.TypePool.Empty
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
    private lateinit var accountMapper: AccountMapper

    @Autowired
    lateinit var userMapper: UserMapper

    private final var passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()

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
            if(!user.name.equals("") && !user.phone.equals("") && !user.nationalId.equals(""))
            {
                user.password = passwordEncoder.encode(user.password)
                user.compactPassword = passwordEncoder.encode(user.compactPassword)
                userMapper.insertUser(user)
            }
    }

    override fun updateUser(user: User) {
            userMapper.updateUser(user)
    }

    override fun deleteUser(id:Long) {
            userMapper.deleteUser(id)
    }

    override fun login(id: String, password: String):Authentication {

        val user:User = userMapper.getUserBySignUpId(id)

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
            return UsernamePasswordAuthenticationToken(user.id, null, roles)
        }
        // password not match
        throw PasswordNotMatchesException("Your password is wrong")
    }

    override fun getBalanceByuserId(id: Long): Long {
            if (accountMapper.isAccountAlreadyExist(id)==0.toLong()) {
                throw BankAccountNotExist("User doen't have account!") //만들어보앗다.
            }else{
                return userMapper.getBalanceByuserId(id)
            }


    }

    override fun isSameKey(user: User, compactPassword: String): Boolean {
        if(passwordEncoder.matches(compactPassword, user.compactPassword))
        {
            return true;
        }
        return false;
    }

}
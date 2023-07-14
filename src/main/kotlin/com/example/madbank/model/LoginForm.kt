package com.example.madbank.model

import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
data class LoginForm (
    val id:String,
        val password:String
)
{
    companion object{
        fun fromJson(json: Map<String, Any>):LoginForm
        {
            val id = (json["id"] as String)
            val password = (json["password"] as String)
            return LoginForm(id, password)
        }
    }
}
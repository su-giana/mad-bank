package com.example.madbank.model

import lombok.AllArgsConstructor
import lombok.Data

@Data
@AllArgsConstructor
data class PayLoginForm (
        val id:String,
        val password:String,
        val pid:Long,
        val aid:Long,
)
{
    companion object{
        fun fromJson(json: Map<String, Any>):PayLoginForm
        {
            val id = (json["id"] as String)
            val password = (json["password"] as String)
            val pid = (json["product"] as Long)
            val aid = (json["account"] as Long)
            return PayLoginForm(id, password, pid, aid)
        }
    }
}
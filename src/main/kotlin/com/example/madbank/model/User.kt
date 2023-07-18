package com.example.madbank.model

import lombok.AllArgsConstructor
import lombok.Data
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@Data
@AllArgsConstructor
data class User (
        @Id
        @GeneratedValue
        var id:Long,

        var name:String,

        var phone:String,

        var password:String,

        var dob:String,

        var nationalId:String,

        var signUpId:String,

        var compactPassword:String
)
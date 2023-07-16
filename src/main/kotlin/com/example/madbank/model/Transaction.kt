package com.example.madbank.model

import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@Data
class Transaction (
    @Id
    @GeneratedValue
    public var transactionId:Long,

    // account
    @Column(name = "sender_id")
    public var senderId:Long,

    //account
    @Column(name = "receiver_id")
    public var receiverId:Long,

    @Column(name = "transaction_type")
    public var transactionType: String,

    @Column(name = "cost")
    public var cost:Long,

    @Column(name = "result_code")
    public var resultCode:String,

)
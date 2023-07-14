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

    @Column
    public var senderId:Long,

    @Column
    public var receiverId:Long,

    @Column
    public var transactionType: String,

    @Column
    public var cost:Long,



)
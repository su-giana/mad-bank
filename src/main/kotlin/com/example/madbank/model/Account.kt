package com.example.madbank.model

import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id


@Entity
@Data
class Account (
    @Id
    @GeneratedValue
    public var accountId: Long,

    @Column
    public var userId:Long,

    @Column
    public var accountNumber:Long,

    @Column
    public var balance:Long,
)
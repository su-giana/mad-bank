package com.example.madbank.model

import lombok.Data
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
@Data
data class Market (
    @Id
    @GeneratedValue
    public var productId: Long,

    @Column
    public var productName:String,

    @Column
    public var productPrice:String,

    @Column
    public var sellerUserId:Long,

    @Column
    public var sellerAccountNumber:String,

    @Column
    public var productImg:String,

)
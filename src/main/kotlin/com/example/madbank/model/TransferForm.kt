package com.example.madbank.model

import lombok.AllArgsConstructor
import lombok.Data
import org.springframework.web.bind.annotation.RequestParam

@Data
@AllArgsConstructor
data class TransferForm (
        val transactionId: Long,
        val transactionType:String,
        val senderAccountId:Long,
        val receiverAccountId:Long,
        val cost:Long
)
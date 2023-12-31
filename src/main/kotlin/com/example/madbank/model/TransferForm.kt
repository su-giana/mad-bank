package com.example.madbank.model

import lombok.AllArgsConstructor
import lombok.Data
import org.springframework.web.bind.annotation.RequestParam

@Data
@AllArgsConstructor
data class TransferForm (
        val transactionType:String,
        val senderAccountId:Long,
        val receiverAccountNumber:String,
        val cost:Long,
        val compactPassword:String
)
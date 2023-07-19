package com.example.madbank.service

import com.example.madbank.model.Account
import com.example.madbank.model.Market

interface MarketService {

    public fun getProductList():List<Market>

    public fun getProductById(id:Long):Market

}
package com.example.madbank.serviceImp

import com.example.madbank.mapper.MarketMapper
import com.example.madbank.model.Market
import com.example.madbank.service.MarketService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MarketServiceImp:MarketService {

    @Autowired
    lateinit var marketMapper: MarketMapper
    override fun getProductList():List<Market> {
        return marketMapper.getProductList()
    }

    override fun getProductById(id: Long): Market {
        return marketMapper.getProductById(id)
    }
}
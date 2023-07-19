package com.example.madbank.mapper

import com.example.madbank.model.Market
import org.apache.ibatis.annotations.Mapper

@Mapper
interface MarketMapper {
    public fun getProductList():List<Market>

    public fun getProductById(id:Long):Market
}
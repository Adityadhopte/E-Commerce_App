package com.example.data.model.response

import com.example.data.model.DataProductModel
import com.example.domain.model.ProductListModel
import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    val `data`: List<DataProductModel>,
    val msg: String
){
    fun toProductList()= ProductListModel(
        products = `data`.map { it.toProduct() },
        msg = msg

    )
}
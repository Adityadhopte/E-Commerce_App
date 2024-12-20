package com.example.domain.network

import com.example.domain.model.CategoryListModule
import com.example.domain.model.Product
import com.example.domain.model.ProductListModel

interface NetworkService {
    suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel>
    suspend fun getCategories():  ResultWrapper<CategoryListModule>
}

sealed class ResultWrapper<out T>{
    data class Success<out T>(val value: T): ResultWrapper<T>()
    data class Failure(val exception: Exception) : ResultWrapper<Nothing>()
}
package com.example.domain.repository

import com.example.domain.model.CategoryListModule
import com.example.domain.network.ResultWrapper

interface CategoryRepository {
    suspend fun getCategories():ResultWrapper<CategoryListModule>
}
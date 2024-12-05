package com.example.data.repository

import com.example.data.model.CategoryDataModule
import com.example.domain.model.CategoryListModule
import com.example.domain.network.NetworkService
import com.example.domain.network.ResultWrapper
import com.example.domain.repository.CategoryRepository

class CategoryRepositoryImpl(val networkService: NetworkService): CategoryRepository {

    override suspend fun getCategories(): ResultWrapper<CategoryListModule> {
        return networkService.getCategories()
    }

}
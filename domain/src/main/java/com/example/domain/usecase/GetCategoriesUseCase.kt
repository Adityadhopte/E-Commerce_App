package com.example.domain.usecase

import com.example.domain.repository.CategoryRepository

class GetCategoriesUseCase(private val categoryRepository: CategoryRepository) {

    suspend fun execute()= categoryRepository.getCategories()
}
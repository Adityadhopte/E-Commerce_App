package com.example.data.di

import com.example.data.repository.CategoryRepositoryImpl
import com.example.data.repository.ProductRepositoryImpl
import com.example.domain.repository.CategoryRepository
import com.example.domain.repository.ProductRepository
import org.koin.dsl.module


val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    single  <CategoryRepository > { CategoryRepositoryImpl(get()) }
}
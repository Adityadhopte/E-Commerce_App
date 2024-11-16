package com.example.data.di

import com.example.data.repository.ProductRepositoryImpl
import com.example.domain.repository.ProductRepository
import org.koin.dsl.module


val repositoryModule = module {
    single<ProductRepository> { ProductRepositoryImpl(get()) }
}
package com.example.ecommerceapp.di

import org.koin.dsl.module

val presentationModule = module {
includes(viewModelModule)
}
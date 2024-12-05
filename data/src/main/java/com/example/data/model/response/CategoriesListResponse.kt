package com.example.data.model.response

import com.example.data.model.CategoryDataModule
import com.example.domain.model.Category
import com.example.domain.model.CategoryListModule
import kotlinx.serialization.Serializable

@Serializable
data class CategoriesListResponse(
    val `data`: List<CategoryDataModule>,
    val msg: String
){

    fun toCategoryList() = CategoryListModule(
        categories = `data`.map { it.toCategory() },
        msg = msg


    )



}
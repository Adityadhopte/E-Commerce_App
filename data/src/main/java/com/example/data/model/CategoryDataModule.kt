package com.example.data.model

import com.example.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDataModule(
    val id: Int,
    val image: String,
    val title: String
){
    fun toCategory()= Category(
        id = id,
        image = image,
        title = title
    )

}
package com.example.ecommerceapp.model

import android.os.Parcelable
import com.example.domain.model.Product
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class UiProductModel (
    val id: Int,
    val title: String,
    val price: Double,
    val categoryId: Int,
    val description: String,
    val image: String
 ): Parcelable {

    companion object{

        fun fromProduct(product: Product) = UiProductModel(
            id = product.id,
            title = product.title,
            price = product.price,
            categoryId = product.categoryId,
            description = product.description,
            image = product.image
        )
    }

 }
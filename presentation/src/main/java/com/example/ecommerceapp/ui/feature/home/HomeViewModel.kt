package com.example.ecommerceapp.ui.feature.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.network.ResultWrapper
import com.example.domain.usecase.GetCategoriesUseCase
import com.example.domain.usecase.GetProductUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getProductUseCase: GetProductUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase)
    : ViewModel() {

    private val _uiState = MutableStateFlow<HomeScreenUIEvents>(HomeScreenUIEvents.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getAllProducts()
    }

    private fun getAllProducts() {
        viewModelScope.launch {
            _uiState.value = HomeScreenUIEvents.Loading
            Log.d("HomeViewModel", "State: Loading")


            val featured = getProducts(1)
            val popularProducts = getProducts(2)
            val categories = getCategories()

            if (featured.isNotEmpty() && popularProducts.isNotEmpty() && categories.isNotEmpty()) {
                _uiState.value = HomeScreenUIEvents.Success(featured, popularProducts , categories)
                return@launch
                Log.d("HomeViewModel", "State: Success")
            } else {
                _uiState.value = HomeScreenUIEvents.Error("Something went wrong")
                Log.d("HomeViewModel", "State: Error")
            }
        }
    }

    private suspend fun getCategories(): List<String> {
        Log.d("HomeViewModel", "Starting getCategories")
        return try {
            getCategoriesUseCase.execute().let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        Log.d("HomeViewModel", "Categories fetched successfully: ${result.value}")
                        result.value.categories.map { it.title }
                    }
                    is ResultWrapper.Failure -> {
                        Log.e("HomeViewModel", "Categories fetching failed: ${result.exception}")
                        emptyList()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Exception in getCategories: ${e.message}")
            emptyList()
        }
    }

    private suspend fun getProducts(category: Int?): List<Product> {
        Log.d("HomeViewModel", "Fetching products for category: $category")
        return try {
            getProductUseCase.execute(category).let { result ->
                when (result) {
                    is ResultWrapper.Success -> {
                        Log.d("HomeViewModel", "Fetched products: ${result.value}")
                        result.value.products
                    }
                    is ResultWrapper.Failure -> {
                        Log.e("HomeViewModel", "Error fetching products: ${result.exception}")
                        emptyList()
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Exception while fetching products: $e")
            emptyList()
        }
    }

    sealed class HomeScreenUIEvents {
        data object Loading : HomeScreenUIEvents()
        data class Success(
            val features: List<Product>,
            val popularProducts: List<Product>,
            val categories: List<String>
        ) : HomeScreenUIEvents()

        data class Error(val message: String) : HomeScreenUIEvents()
    }
}

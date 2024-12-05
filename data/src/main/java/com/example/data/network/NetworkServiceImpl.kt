package com.example.data.network

import android.util.Log
import com.example.data.model.response.CategoriesListResponse
import com.example.data.model.response.ProductListResponse
import com.example.domain.model.CategoryListModule
import com.example.domain.model.ProductListModel
import com.example.domain.network.NetworkService
import com.example.domain.network.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.util.InternalAPI
import java.io.IOException

class NetworkServiceImpl(val client: HttpClient) : NetworkService {
    private val baseUrl = "https://ecommerce-ktor-4641e7ff1b63.herokuapp.com"

    override suspend fun getProducts(category: Int?): ResultWrapper<ProductListModel> {
        // Modify the URL based on the category
        val url =  if (category != null) "$baseUrl/products/category/$category" else "$baseUrl/products"

        Log.d("NetworkServiceImpl", "Requesting URL: $url")

        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { dataModels: ProductListResponse ->
                dataModels.toProductList()
            }
        )
    }

    override suspend fun getCategories(): ResultWrapper<CategoryListModule> {
        val url = "$baseUrl/categories"
        return makeWebRequest(
            url = url,
            method = HttpMethod.Get,
            mapper = { categories: CategoriesListResponse ->
                categories.toCategoryList()

    })
    }

    @OptIn(InternalAPI::class)
    suspend inline fun <reified T, R> makeWebRequest(
        url: String,
        method: HttpMethod,
        body: Any? = null,
        headers: Map<String, String> = emptyMap(),
        parameters: Map<String, String> = emptyMap(),
        noinline mapper: ((T) -> R)? = null
    ): ResultWrapper<R> {
        return try {
            // Perform the HTTP request
            val response = client.request(url) {
                this.method = method
                // Apply query parameters (if any)
                url {
                    this.parameters.appendAll(Parameters.build {
                        parameters.forEach { (key, value) ->
                            append(key, value)
                        }
                    })
                }
                // Apply custom headers (if any)
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                // Set body if needed (e.g., POST requests)
                if (body != null) {
                    this.body = body
                }
                // Set the content type
                contentType(ContentType.Application.Json)
            }.body<T>()  // Get the body of the response

            val result: R = mapper?.invoke(response) ?: response as R
            ResultWrapper.Success(result)
        } catch (e: ClientRequestException) {
            Log.e("NetworkServiceImpl", "ClientRequestException: ${e.message}")
            ResultWrapper.Failure(e)
        } catch (e: ServerResponseException) {
            Log.e("NetworkServiceImpl", "ServerResponseException: ${e.message}")
            ResultWrapper.Failure(e)
        } catch (e: IOException) {
            Log.e("NetworkServiceImpl", "IOException: ${e.message}")
            ResultWrapper.Failure(e)
        } catch (e: Exception) {
            Log.e("NetworkServiceImpl", "Exception: ${e.message}")
            ResultWrapper.Failure(e)
        }
    }
}

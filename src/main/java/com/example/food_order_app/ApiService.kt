package com.example.food_order_app

import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("products")
    fun getProducts(): Call<List<Product>>

    @POST("orders")
    fun createOrder(@Body order: OrderRequest): Call<OrderResponse>

    @GET("orders/{id}")
    fun getOrderDetails(@Path("id") id: Int): Call<OrderDetails>
}
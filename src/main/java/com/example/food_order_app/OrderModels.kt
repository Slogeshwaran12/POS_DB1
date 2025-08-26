package com.example.food_order_app

data class OrderRequest(
    val items: List<OrderItem>,
    val total: Double
)

data class OrderItem(
    val productId: Int,
    val quantity: Int,
    val price: Double
)

data class OrderResponse(
    val id: Int,
    val message: String,
    val success: Boolean
)

data class OrderDetails(
    val id: Int,
    val total: Double,
    val items: List<OrderItem>,
    val status: String,
    val createdAt: String
)
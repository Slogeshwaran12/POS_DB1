package com.example.food_order_app

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderDetailsActivity : AppCompatActivity() {

    private lateinit var orderDetailsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_details)

        orderDetailsText = findViewById(R.id.orderDetailsText)

        val orderId = intent.getIntExtra("ORDER_ID", -1)
        if (orderId != -1) {
            loadOrderDetails(orderId)
        } else {
            Toast.makeText(this, "Invalid Order ID", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadOrderDetails(orderId: Int) {
        ApiClient.apiService.getOrderDetails(orderId).enqueue(object : Callback<OrderDetails> {
            override fun onResponse(call: Call<OrderDetails>, response: Response<OrderDetails>) {
                if (response.isSuccessful) {
                    val order = response.body()
                    val itemsText = order?.items?.joinToString(", ") { "Item ${it.productId} x${it.quantity}" } ?: "No items"
                    orderDetailsText.text = "Order ID: ${order?.id}\nTotal: ₹${order?.total}\nItems: $itemsText\nStatus: ${order?.status}"
                    Toast.makeText(this@OrderDetailsActivity, "Order details loaded", Toast.LENGTH_SHORT).show()
                    Log.d("API_ORDER_DETAILS", "Order details: $order")
                } else {
                    Log.e("API_ORDER_DETAILS", "Failed code: ${response.code()}")
                    Toast.makeText(this@OrderDetailsActivity, "Failed to load details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OrderDetails>, t: Throwable) {
                Log.e("API_ORDER_DETAILS", "Error: ${t.message}")
                Toast.makeText(this@OrderDetailsActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
package com.example.food_order_app

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var checkoutButton: Button
    private lateinit var totalText: TextView

    private val selectedProducts = mutableMapOf<Product, Int>()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        checkoutButton = findViewById(R.id.btnCheckout)
        totalText = findViewById(R.id.totalText)

        productAdapter = ProductAdapter { product ->
            val count = selectedProducts.getOrDefault(product, 0) + 1
            selectedProducts[product] = count
            updateTotal()
        }

        recyclerView.layoutManager = GridLayoutManager(this, 2)
        recyclerView.adapter = productAdapter

        loadProducts()

        checkoutButton.setOnClickListener {
            if (selectedProducts.isEmpty()) {
                Toast.makeText(this, "Select at least one product", Toast.LENGTH_SHORT).show()
            } else {
                showOrderPopup()
            }
        }
    }

    private fun loadProducts() {
        ApiClient.apiService.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful) {
                    val products = response.body() ?: emptyList()
                    productAdapter.submitList(products)
                } else {
                    Toast.makeText(this@MainActivity, "Error fetching products", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateTotal() {
        val total = selectedProducts.entries.sumOf { it.key.price * it.value }
        totalText.text = "Total: ₹$total"
        checkoutButton.text = "Check Out"
    }

    private fun showOrderPopup() {
        val builder = AlertDialog.Builder(this)
        val orderSummary = selectedProducts.entries.joinToString("\n") {
            "${it.key.name} x${it.value} - ₹${it.key.price * it.value}"
        }
        val total = selectedProducts.entries.sumOf { it.key.price * it.value }

        builder.setTitle("🎉 Your Order Summary 🎉")
        builder.setMessage("$orderSummary\n\nTotal: ₹$total\n\nConfirm your order?")
        builder.setPositiveButton("Confirm") { dialog, _ ->

            // Prepare API request
            val orderItems = selectedProducts.map { (product, qty) ->
                OrderItem(
                    productId = product.id,
                    quantity = qty,
                    price = product.price
                )
            }

            val orderRequest = OrderRequest(
                items = orderItems,
                total = total
            )

            // Send to backend
            ApiClient.apiService.createOrder(orderRequest).enqueue(object : Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "✅ Order Placed Successfully!", Toast.LENGTH_LONG).show()
                        selectedProducts.clear()
                        productAdapter.clearSelection()
                        updateTotal()
                    } else {
                        val errorMsg = response.errorBody()?.string()
                        Toast.makeText(this@MainActivity, "Failed to place order: $errorMsg", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })

            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}

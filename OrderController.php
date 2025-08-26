<?php

namespace App\Http\Controllers;

use App\Models\Order;
use App\Models\OrderItem;
use App\Models\Product;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

class OrderController extends Controller
{
    // Store new order
    public function store(Request $request)
    {
        try {
            Log::info('Order request received:', $request->all());

            // Validation
            $request->validate([
                'items' => 'required|array|min:1',
                'items.*.product_id' => 'required|integer|exists:products,id',
                'items.*.quantity' => 'required|integer|min:1',
            ]);

            $total = 0;

            // Calculate total
            foreach ($request->items as $item) {
                $product = Product::find($item['product_id']);
                $total += $product->price * $item['quantity'];
            }

            // Create order
            $order = Order::create([
                'customer_name' => 'Android User',
                'total_amount' => $total,
            ]);

            // Create order items
            foreach ($request->items as $item) {
                $product = Product::find($item['product_id']);

                OrderItem::create([
                    'order_id' => $order->id,
                    'product_id' => $item['product_id'],
                    'quantity' => $item['quantity'],
                    'price' => $product->price,
                ]);
            }

            return response()->json([
                'id' => $order->id,
                'status' => 'success'
            ]);

        } catch (\Exception $e) {
            Log::error('Order creation error:', ['message' => $e->getMessage()]);
            return response()->json(['error' => 'Order creation failed: ' . $e->getMessage()], 500);
        }
    }

    // Show order details
    public function show($id)
    {
        try {
            $order = Order::with('items.product')->findOrFail($id);

            return response()->json([
                'id' => $order->id,
                'items' => $order->items->map(function($item) {
                    return [
                        'id' => $item->product->id,
                        'name' => $item->product->name,
                        'price' => $item->product->price,
                        'quantity' => $item->quantity,
                    ];
                }),
                'total' => $order->total_amount,
            ]);

        } catch (\Exception $e) {
            return response()->json(['error' => 'Order not found'], 404);
        }
    }
}

<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Models\Product;

class ProductController extends Controller
{
    public function index()
    {
        $products = Product::all()->map(function ($product) {

            // Updated image filenames based on your folder
         $imageMap =$imageMap = [
                                'Burger'     => 'burger.jpg',
                                'Pizza'      => 'pizza.jpg',
                                'Pasta'      => 'pasta.jpg',
                                'Sandwich'   => 'sandwich.jpg',
                                'Coffee'     => 'coffee.jpg',
                                'Ice Cream'  => 'icecream.jpg',
                                'Donut'      => 'donut.jpg',
                                'Cake'       => 'cake.jpg',
                                'Brownie'    => 'brownie.jpg',
                                'Milkshake'  => 'milkshake.jpg',
];



            return [
                'id' => $product->id,
                'name' => $product->name,
                'price' => $product->price,
                'description' => $product->description,
                'imageUrl' => url('images/products/' . ($imageMap[$product->name] ?? 'placeholder.png')),
            ];
        });

        return response()->json($products);
    }
}

<?php
// database/seeders/DatabaseSeeder.php
use Illuminate\Database\Seeder;
use App\Models\Product;
use App\Models\User;

class DatabaseSeeder extends Seeder
{
    public function run(): void
    {
        User::factory()->create([
            'name' => 'Test User',
            'email' => 'test@example.com',
            'password' => bcrypt('password'),
        ]);

        $products = [
            ['name'=>'Burger','price'=>50,'description'=>'Juicy beef burger with fresh veggies','image'=>'burger.png'],
            ['name'=>'Pizza','price'=>150,'description'=>'Cheesy Margherita pizza with tomato sauce','image'=>'pizza.png'],
            ['name'=>'Pasta','price'=>120,'description'=>'Italian pasta with creamy Alfredo sauce','image'=>'pasta.png'],
            ['name'=>'Sandwich','price'=>80,'description'=>'Grilled sandwich with lettuce & tomato','image'=>'sandwich.png'],
            ['name'=>'Coffee','price'=>40,'description'=>'Freshly brewed coffee with aroma','image'=>'coffee.png'],
            ['name'=>'Ice Cream','price'=>70,'description'=>'Vanilla ice cream with chocolate syrup','image'=>'icecream.png'],
            ['name'=>'Donut','price'=>35,'description'=>'Soft donut with sugar glaze','image'=>'donut.png'],
            ['name'=>'Cake','price'=>150,'description'=>'Chocolate cake slice with cream','image'=>'cake.png'],
            ['name'=>'Brownie','price'=>60,'description'=>'Fudgy chocolate brownie','image'=>'brownie.png'],
            ['name'=>'Milkshake','price'=>80,'description'=>'Vanilla milkshake with whipped cream','image'=>'milkshake.png'],
        ];

        foreach($products as $product){
            Product::firstOrCreate(['name'=>$product['name']], $product);
        }
    }
}

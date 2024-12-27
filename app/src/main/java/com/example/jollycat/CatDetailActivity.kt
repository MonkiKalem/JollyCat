package com.example.jollycat

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatDetailActivity : AppCompatActivity() {

    private lateinit var catNameTextView: TextView
    private lateinit var catDescriptionTextView: TextView
    private lateinit var catPriceTextView: TextView
    private lateinit var quantityEditText: EditText
    private lateinit var buyButton: Button
    private lateinit var catImageView: ImageView
    private lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cat_detail)

        repository = Repository(this)

        catNameTextView = findViewById(R.id.catNameTextView)
        catDescriptionTextView = findViewById(R.id.catDescriptionTextView)
        catPriceTextView = findViewById(R.id.catPriceTextView)
        quantityEditText = findViewById(R.id.quantityEditText)
        buyButton = findViewById(R.id.buyButton)
        catImageView = findViewById(R.id.catImageView)

        val catId = intent.getStringExtra("CAT_ID")
        val catName = intent.getStringExtra("CAT_NAME")
        val catDescription = intent.getStringExtra("CAT_DESCRIPTION")
        val catPrice = intent.getIntExtra("CAT_PRICE", 0)
        val catImageUrl = intent.getStringExtra("CAT_IMAGE_URL")
        Log.d("DashboardActivity", "Navigating to CatDetailActivity with cat: $catId")

        catNameTextView.text = catName
        catDescriptionTextView.text = catDescription
        catPriceTextView.text = getString(R.string.price_format, catPrice)

        Glide.with(this)
            .load(catImageUrl)
            .into(catImageView)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        buyButton.setOnClickListener {
            val quantityStr = quantityEditText.text.toString()
            if (quantityStr.isEmpty()) {
                Toast.makeText(this, "Please enter quantity", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val quantity = quantityStr.toInt()
            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            Log.d("CatDetailActivity", "Adding to cart: catId=$catId, catName=$catName, catPrice=$catPrice, quantity=$quantity")

            if (catId != null) {
                addToCart(catId, catName ?: "", catPrice, quantity)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun addToCart(catId: String, catName: String, catPrice: Int, quantity: Int) {
        val subtotal = catPrice * quantity
        val sessionManager = SessionManager(this)
        val userId = sessionManager.getUserId() // Retrieve the current user's ID
        val cartId = sessionManager.getCartId() // Retrieve or generate the current cart's ID

        val cartItem = CartItem(
            CatID = catId,
            CartID = cartId,
            CheckoutID = "", // Assuming this will be assigned later
            UserID = userId,
            Quantity = quantity,
            Subtotal = subtotal,
            CatPrice = catPrice
        )

        CoroutineScope(Dispatchers.IO).launch {
            val rowId = repository.insertCartItem(cartItem)
            withContext(Dispatchers.Main) {
                Log.d("CatDetailActivity", "Cart item inserted with rowId: $rowId, cartItem: $cartItem")
                val message = "Added $quantity $catName(s) to cart"
                Toast.makeText(this@CatDetailActivity, message, Toast.LENGTH_SHORT).show()
            }
        }
    }



}

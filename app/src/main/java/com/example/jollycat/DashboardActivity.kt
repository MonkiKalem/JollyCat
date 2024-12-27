package com.example.jollycat

import android.content.Intent
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.math.log

class DashboardActivity : AppCompatActivity() {

    private lateinit var homeRecyclerView: RecyclerView
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var profileUsername: TextView
    private lateinit var profilePhoneNumber: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnCheckout: Button
    private lateinit var homeOption: TextView
    private lateinit var cartOption: TextView
    private lateinit var profileOption: TextView
    private lateinit var homeSection: RelativeLayout
    private lateinit var cartSection: RelativeLayout
    private lateinit var profileSection: RelativeLayout
    private lateinit var burgerIcon: View
    private lateinit var totalSubtotal: TextView
    private lateinit var cartAdapter: CartItemAdapter

    private val sessionManager by lazy { SessionManager(this) } // Instantiate SessionManager
    private val repository by lazy { Repository(this) }
    private lateinit var requestQueue: RequestQueue
    private var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userId = sessionManager.getUserId()
        requestQueue = Volley.newRequestQueue(this)

        homeRecyclerView = findViewById(R.id.homeRecyclerView)
        cartRecyclerView = findViewById(R.id.cartRecyclerView)
        profileUsername = findViewById(R.id.profileUsername)
        profilePhoneNumber = findViewById(R.id.profilePhoneNumber)
        btnLogout = findViewById(R.id.btnLogout)
        btnCheckout = findViewById(R.id.btnCheckout)
        homeOption = findViewById(R.id.homeOption)
        cartOption = findViewById(R.id.cartOption)
        profileOption = findViewById(R.id.profileOption)
        totalSubtotal = findViewById(R.id.totalSubtotal)

        homeSection = findViewById(R.id.homeSection)
        cartSection = findViewById(R.id.cartSection)
        profileSection = findViewById(R.id.profileSection)
        burgerIcon = findViewById(R.id.burgerIcon)

        homeOption.setOnClickListener {
            showHomeSection()
        }
        cartOption.setOnClickListener {
            showCartSection()
        }
        profileOption.setOnClickListener {
            showProfileSection()
        }

        homeRecyclerView.layoutManager = LinearLayoutManager(this)
        cartRecyclerView.layoutManager = LinearLayoutManager(this)

        fetchCatData { cat ->
            navigateToCatDetail(cat)
        }
        fetchCartData()

        displayUserInfo()

        btnLogout.setOnClickListener {
            logout()
        }

        btnCheckout.setOnClickListener {
            checkout()
        }

        burgerIcon.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    override fun onResume() {
        super.onResume()
        refreshCart()
    }


    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.inflate(R.menu.menu_main)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_dashboard -> {
                    navigateToDashboard()
                    true
                }
                R.id.action_about_us -> {
                    navigateToAboutUs()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToAboutUs() {
        val intent = Intent(this, AboutUsActivity::class.java)
        startActivity(intent)
    }

    private fun showHomeSection() {
        homeSection.visibility = View.VISIBLE
        cartSection.visibility = View.GONE
        profileSection.visibility = View.GONE
    }

    private fun showCartSection() {
        homeSection.visibility = View.GONE
        cartSection.visibility = View.VISIBLE
        profileSection.visibility = View.GONE
    }

    private fun showProfileSection() {
        homeSection.visibility = View.GONE
        cartSection.visibility = View.GONE
        profileSection.visibility = View.VISIBLE
    }

    private fun fetchCatData(onItemClick: (Cat) -> Unit) {
        val url = "https://api.npoint.io/3fa9a95557f89f097063"

        val request = JsonArrayRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val catData = JsonParser.parseCatData(response)

                CoroutineScope(Dispatchers.IO).launch {
                    storeCatDataInDatabase(catData)
                    val cats = repository.getAllCats()

                    withContext(Dispatchers.Main) {
                        val homeAdapter = CatAdapter(cats, onItemClick)
                        homeRecyclerView.adapter = homeAdapter
                    }
                }
            },
            Response.ErrorListener { error ->
                Log.e("NetworkError", error.toString())
            }
        )
        requestQueue.add(request)
    }

    private fun navigateToCatDetail(cat: Cat) {
        val intent = Intent(this@DashboardActivity, CatDetailActivity::class.java).apply {
            putExtra("CAT_ID", cat.CatID)
            putExtra("CAT_NAME", cat.CatName)
            putExtra("CAT_DESCRIPTION", cat.CatDescription)
            putExtra("CAT_PRICE", cat.CatPrice)
            putExtra("CAT_IMAGE_URL", cat.CatImage)
        }
        Log.d("DashboardActivity", "Navigating to CatDetailActivity with cat: $cat")
        startActivity(intent)
    }

    private suspend fun storeCatDataInDatabase(catData: List<Cat>) {
        for (cat in catData) {
            val existingCat = repository.getCatByName(cat.CatName)
            if (existingCat == null) {
                repository.insertCat(cat)
            } else {
                Log.d("DuplicateCat", "Cat '${cat.CatName}' already exists in the database.")
            }
        }
    }

    private fun fetchCartData() {
        CoroutineScope(Dispatchers.IO).launch {
            userId?.let {
                val cartItems = repository.getCartItemsByUserId(it)
                withContext(Dispatchers.Main) {
                    if (cartItems.isNotEmpty()) {
                        // Initialize the cartAdapter before accessing it
                        cartAdapter = CartItemAdapter(this@DashboardActivity, cartItems, this@DashboardActivity.lifecycle) {
                            updateTotalSubtotal(cartItems)
                            cartAdapter.updateCartItems(cartItems)
                        }
                        // Set the adapter to the RecyclerView
                        cartRecyclerView.adapter = cartAdapter
                        // Notify the adapter of the dataset changes
                        cartAdapter.notifyDataSetChanged()
                        // Update total subtotal
                        updateTotalSubtotal(cartItems)
                        // Update the UI with the new cart items
                    } else {
                        Log.d("DashboardActivity", "No cart items found")
                    }
                }
            }
        }
    }

    private fun updateTotalSubtotal(cartItems: List<CartItem>) {
        val total = cartItems.sumOf { it.Subtotal }
        totalSubtotal.text = getString(R.string.subtotal_format, total)
    }


    private fun displayUserInfo() {
        Log.d("DashboardActivity", "Displaying user info...")
        CoroutineScope(Dispatchers.IO).launch {
            userId?.let {
                val user = repository.getUserById(it)
                Log.d("DashboardActivity", "User data: $user")
                withContext(Dispatchers.Main) {
                    user?.let { userData ->
                        Log.d("DashboardActivity", "Username: ${userData.Username}, Phone Number: ${userData.PhoneNumber}")
                        profileUsername.text = userData.Username
                        profilePhoneNumber.text = userData.PhoneNumber
                    }
                }
            }
        }
    }


    private fun refreshCart() {
        fetchCartData()
    }


    private fun logout() {
        // Clear the user session
        sessionManager.clearSession()

        // Navigate to the login screen or perform any other necessary cleanup tasks
        val intent = Intent(this@DashboardActivity, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish() // Finish the current activity to prevent the user from navigating back to it
    }

    private fun generateCheckoutId(): String {
        return UUID.randomUUID().toString()
    }

    private fun checkout() {
        CoroutineScope(Dispatchers.IO).launch {
            userId?.let {
                val cartItems = repository.getCartItemsByUserId(it)
                withContext(Dispatchers.Main) {
                    if (cartItems.isNotEmpty()) {
                        val checkoutId = generateCheckoutId()
                        for (cartItem in cartItems) {
                            cartItem.CheckoutID = checkoutId

                            // Update the cart item in the database with the checkout ID
                            repository.updateCartItem(cartItem)

                            // Log the checked out item
                            Log.d("Checkout", "'${cartItem.Quantity}' Items '${cartItem.CartID}' checked out with checkout ID: $checkoutId")

                        }

                        // Get the user's phone number
                        val user = repository.getUserById(userId!!)
                        user?.let {
                            val phoneNumber = user.PhoneNumber
                            // Send SMS notification
                            sendSMS(phoneNumber, "Your checkout is successful. Cart ID: ${cartItems.firstOrNull()?.CartID}, Checkout ID: $checkoutId")
                        }

                        // Reload the activity
                        reloadPage()

                    } else {
                        Log.d("DashboardActivity", "No cart items found")
                    }
                }
            }
        }

    }

    fun reloadPage() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
        showCartSection()
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val smsManager = SmsManager.getDefault()
        smsManager.sendTextMessage(phoneNumber, null, message, null, null)
    }









}

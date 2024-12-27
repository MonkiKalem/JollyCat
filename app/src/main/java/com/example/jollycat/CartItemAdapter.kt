package com.example.jollycat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CartItemAdapter(
    private val context: Context,
    private var cartItems: List<CartItem>,
    private val lifecycle: Lifecycle,
    private val updateSubtotal: () -> Unit
) : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    private val repository = Repository(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false)
        return CartItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        val cartItem = cartItems[position]
        holder.bind(cartItem)
    }

    override fun getItemCount(): Int {
        return cartItems.size
    }

    inner class CartItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvCatName: TextView = itemView.findViewById(R.id.tvCatName)
        private val tvCatPrice: TextView = itemView.findViewById(R.id.tvCatPrice)
        private val etQuantity: EditText = itemView.findViewById(R.id.etQuantity)
        private val catImageView: ImageView = itemView.findViewById(R.id.catImageView)

        fun bind(cartItem: CartItem) {
            val lifecycleScope = lifecycle.coroutineScope

            lifecycleScope.launch(Dispatchers.IO) {
                val cat = repository.getCatById(cartItem.CatID)
                withContext(Dispatchers.Main) {
                    cat?.let {
                        tvCatName.text = it.CatName
                        tvCatPrice.text = context.getString(R.string.price_format, it.CatPrice)
                        Glide.with(context)
                            .load(it.CatImage)
                            .apply(RequestOptions().placeholder(R.drawable.cat_placeholder_background))
                            .into(catImageView)
                    }

                    etQuantity.setText(cartItem.Quantity.toString())

                    etQuantity.setOnFocusChangeListener { _, hasFocus ->
                        if (!hasFocus) {
                            val newQuantity = etQuantity.text.toString().toIntOrNull()
                            if (newQuantity != null && newQuantity > 0) {
                                lifecycleScope.launch(Dispatchers.IO) {
                                    val updatedCartItem = cartItem.copy(Quantity = newQuantity)
                                    updatedCartItem.Subtotal = updatedCartItem.CatPrice * updatedCartItem.Quantity
                                    repository.updateCartItem(updatedCartItem)

                                    withContext(Dispatchers.Main) {
                                        updateSubtotal() // Update the subtotal when quantity changes
                                        (context as? DashboardActivity)?.reloadPage()
                                    }
                                }
                            } else {
                                etQuantity.setText(cartItem.Quantity.toString())
                                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }

    // Update cart items list when it changes
    fun updateCartItems(newCartItems: List<CartItem>) {
        cartItems = newCartItems
        notifyDataSetChanged()
    }
}

package com.example.jollycat

import androidx.room.*

@Dao
interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cartItem: CartItem): Long

    @Update
    fun update(cartItem: CartItem)

    @Delete
    fun delete(cartItem: CartItem)

    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): List<CartItem>


    @Query("SELECT * FROM cart_items WHERE CartID = :cartId")
    fun getCartItemsByCartId(cartId: String): List<CartItem>

    @Query("SELECT * FROM cart_items WHERE UserID = :userId AND (CheckoutID IS NULL OR CheckoutID = '')")
    fun getCartItemsByUserId(userId: String): List<CartItem>

    @Query("UPDATE cart_items SET Quantity = :quantity WHERE CartID = :cartId")
    fun updateCartItemQuantity(cartId: String, quantity: Int)
}


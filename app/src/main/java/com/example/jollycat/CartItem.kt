package com.example.jollycat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val CartID: String,
    val CatID: String,
    var CheckoutID: String,
    val UserID: String,
    val Quantity: Int,
    var Subtotal: Int,
    val CatPrice: Int
)




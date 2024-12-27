package com.example.jollycat

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "USER_ID"
        private const val KEY_CART_ID = "CART_ID"
    }

    fun saveUserId(userId: String) {
        with(prefs.edit()) {
            putString(KEY_USER_ID, userId)
            apply()
        }
    }

    fun getUserId(): String {
        return prefs.getString(KEY_USER_ID, "") ?: ""
    }

    fun saveCartId(cartId: String) {
        with(prefs.edit()) {
            putString(KEY_CART_ID, cartId)
            apply()
        }
    }

    fun getCartId(): String {
        var cartId = prefs.getString(KEY_CART_ID, "")
        if (cartId.isNullOrEmpty()) {
            cartId = generateUniqueCartId()
            saveCartId(cartId)
        }
        return cartId
    }

    private fun generateUniqueCartId(): String {
        return java.util.UUID.randomUUID().toString()
    }

    fun clearSession() {
        with(prefs.edit()) {
            remove(KEY_USER_ID)
            remove(KEY_CART_ID)
            apply()
        }
    }
}

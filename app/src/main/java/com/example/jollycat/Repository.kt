package com.example.jollycat

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) {

    private val db = MyDatabase.getInstance(context)

    suspend fun updateUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDao().update(user)
        }
    }

    suspend fun insertUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDao().insert(user)
        }
    }

    suspend fun deleteUser(user: User) {
        withContext(Dispatchers.IO) {
            db.userDao().delete(user)
        }
    }

    suspend fun getUserByUsername(username: String): User? {
        return withContext(Dispatchers.IO) {
            db.userDao().getUserByUsername(username)
        }
    }

    suspend fun getUserById(userId: String): User? {
        return withContext(Dispatchers.IO) {
            db.userDao().getUserById(userId)
        }
    }

    suspend fun getCatByName(catName: String): Cat? {
        return withContext(Dispatchers.IO) {
            db.catDao().getCatByName(catName)
        }
    }

    suspend fun insertCat(cat: Cat) {
        withContext(Dispatchers.IO) {
            db.catDao().insert(cat)
        }
    }

    suspend fun getAllCats(): List<Cat> {
        return withContext(Dispatchers.IO) {
            db.catDao().getAllCats()
        }
    }

    suspend fun getCartItemsByUserId(userId: String): List<CartItem> {
        return withContext(Dispatchers.IO) {
            db.cartItemDao().getCartItemsByUserId(userId)
        }
    }

    suspend fun updateCartItem(cartItem: CartItem) {
        withContext(Dispatchers.IO) {
            db.cartItemDao().update(cartItem)
        }
    }

    suspend fun insertCartItem(cartItem: CartItem): Long {
        return withContext(Dispatchers.IO) {
            val rowId = db.cartItemDao().insert(cartItem)
            Log.d("Repository", "Inserted cart item with rowId: $rowId, cartItem: $cartItem")
            rowId
        }
    }

    suspend fun getAllCartItems(): List<CartItem> {
        return withContext(Dispatchers.IO) {
            db.cartItemDao().getAllCartItems()
        }
    }

    suspend fun getCatById(catId: String?): Cat? {
        return withContext(Dispatchers.IO) {
            db.catDao().getCatById(catId)
        }
    }
}

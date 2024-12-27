package com.example.jollycat

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "JollyCatDB"
        private const val DATABASE_VERSION = 1

        // Table names
        private const val TABLE_USERS = "Users"
        private const val TABLE_CART = "CartData"
        private const val TABLE_CATS = "Cats"

        // Common column names
        private const val KEY_ID = "id"

        // Users table column names
        private const val KEY_USER_ID = "UserID"
        private const val KEY_USERNAME = "Username"
        private const val KEY_PHONE_NUMBER = "PhoneNumber"
        private const val KEY_PASSWORD = "Password"

        // CartData table column names
        private const val KEY_CART_ID = "CartID"
        private const val KEY_CAT_ID = "CatID"
        private const val KEY_CHECKOUT_ID = "CheckoutID"
        private const val KEY_QUANTITY = "Quantity"

        // Cats table column names
        private const val KEY_CAT_NAME = "CatName"
        private const val KEY_CAT_DESCRIPTION = "CatDescription"
        private const val KEY_CAT_IMAGE = "CatImage"
        private const val KEY_CAT_PRICE = "CatPrice"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Creating Users table
        val CREATE_USERS_TABLE = ("CREATE TABLE $TABLE_USERS("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_USER_ID TEXT,"
                + "$KEY_USERNAME TEXT,"
                + "$KEY_PHONE_NUMBER TEXT,"
                + "$KEY_PASSWORD TEXT)")

        // Creating CartData table
        val CREATE_CART_TABLE = ("CREATE TABLE $TABLE_CART("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_CART_ID TEXT,"
                + "$KEY_CAT_ID TEXT,"
                + "$KEY_CHECKOUT_ID TEXT,"
                + "$KEY_USER_ID TEXT,"
                + "$KEY_QUANTITY INTEGER)")

        // Creating Cats table
        val CREATE_CATS_TABLE = ("CREATE TABLE $TABLE_CATS("
                + "$KEY_ID INTEGER PRIMARY KEY,"
                + "$KEY_CAT_ID TEXT,"
                + "$KEY_CAT_NAME TEXT,"
                + "$KEY_CAT_DESCRIPTION TEXT,"
                + "$KEY_CAT_IMAGE TEXT,"
                + "$KEY_CAT_PRICE INTEGER)")

        db?.execSQL(CREATE_USERS_TABLE)
        db?.execSQL(CREATE_CART_TABLE)
        db?.execSQL(CREATE_CATS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older tables if existed
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CATS")

        // Create tables again
        onCreate(db)
    }

    // Add methods to perform database operations like add, delete, update, and retrieve
}

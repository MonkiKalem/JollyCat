package com.example.jollycat

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, CartItem::class, Cat::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun cartItemDao(): CartItemDao
    abstract fun catDao(): CatDao

    companion object {
        @Volatile
        private var INSTANCE: MyDatabase? = null

        fun getInstance(context: Context): MyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java,
                    "jollycat_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

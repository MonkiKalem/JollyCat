package com.example.jollycat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class User(
    @PrimaryKey val UserID: String,
    val Username: String,
    val PhoneNumber: String,
    val Password: String
)


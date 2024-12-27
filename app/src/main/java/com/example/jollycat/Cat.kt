package com.example.jollycat

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Cats")
data class Cat(
    @PrimaryKey val CatID: String,
    val CatName: String,
    val CatImage: String,
    val CatPrice: Int,
    val CatDescription: String = ""
)

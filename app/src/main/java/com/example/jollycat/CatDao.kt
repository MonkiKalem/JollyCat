package com.example.jollycat

import androidx.room.*

@Dao
interface CatDao {
    @Insert
    fun insert(cat: Cat)

    @Update
    fun update(cat: Cat)

    @Delete
    fun delete(cat: Cat)

    @Query("SELECT * FROM Cats")
    fun getAllCats(): List<Cat>

    @Query("SELECT * FROM Cats WHERE CatID = :catId")
    fun getCatById(catId: String?): Cat?

    @Query("SELECT * FROM Cats WHERE catName = :catName")
    suspend fun getCatByName(catName: String): Cat?

}

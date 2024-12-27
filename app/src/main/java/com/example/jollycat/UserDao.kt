package com.example.jollycat

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    fun insert(user: User)

    @Update
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM Users WHERE userId = :userId")
    fun getUserById(userId: String): User?

    @Query("SELECT * FROM Users WHERE Username = :username")
    fun getUserByUsername(username: String): User?

    

}

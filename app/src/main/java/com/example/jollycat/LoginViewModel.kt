package com.example.jollycat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: Repository) : ViewModel() {

    suspend fun getUserById(userId: String) = repository.getUserById(userId)

    suspend fun insertUser(user: User) {
        repository.insertUser(user)
    }

    suspend fun updateUser(user: User) {
        repository.updateUser(user)
    }

    suspend fun deleteUser(user: User) {
        repository.deleteUser(user)
    }

    suspend fun login(username: String, password: String): Boolean {
        val user = repository.getUserByUsername(username)
        return user != null && user.Password == password
    }

    fun logout() {
        // Perform logout logic here
        // Example: Clear session data or perform any necessary cleanup
    }
}

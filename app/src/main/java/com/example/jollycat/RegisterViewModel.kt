package com.example.jollycat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: Repository) : ViewModel() {

    fun insertUser(user: User) {
        viewModelScope.launch {
            repository.insertUser(user)
        }
    }
}

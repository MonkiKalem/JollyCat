package com.example.jollycat

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var btnRegister: Button
    private lateinit var btnLogin: Button

    private val repository = Repository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etUsername = findViewById(R.id.etUsername)
        etPassword = findViewById(R.id.etPassword)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        btnRegister = findViewById(R.id.btnRegister)
        btnLogin = findViewById(R.id.btnLogin)

        btnRegister.setOnClickListener {
            register()
        }

        btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()

        if (isValidRegistration(username, password, phoneNumber)) {
            CoroutineScope(Dispatchers.Main).launch {
                val existingUser = repository.getUserByUsername(username)
                if (existingUser != null) {
                    Toast.makeText(this@RegisterActivity, "Username already exists", Toast.LENGTH_SHORT).show()
                } else {
                    val user = User(
                        UserID = UUID.randomUUID().toString(),
                        Username = username,
                        Password = password,
                        PhoneNumber = phoneNumber
                    )
                    repository.insertUser(user)
                    Toast.makeText(this@RegisterActivity, "Registration successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }
            }
        }
    }

    private fun isValidRegistration(username: String, password: String, phoneNumber: String): Boolean {
        return when {
            username.isBlank() || password.isBlank() || phoneNumber.isBlank() -> {
                showError("All fields must be filled.")
                false
            }
            username.length < 8 -> {
                showError("Username must be at least 8 characters long.")
                false
            }
            !username.matches("^[a-zA-Z0-9]+$".toRegex()) -> {
                showError("Username must be alphanumeric.")
                false
            }
            password.length < 5 -> {
                showError("Password must be at least 5 characters long.")
                false
            }
            !password.matches(".*[A-Za-z].*".toRegex()) || !password.matches(".*[0-9].*".toRegex()) || !password.matches(".*[!@#\$%^&*(),.?\":{}|<>].*".toRegex()) -> {
                showError("Password must contain at least one letter, one number, and one symbol.")
                false
            }
            phoneNumber.length !in 8..20 -> {
                showError("Phone number must be between 8 and 20 characters long.")
                false
            }
            !(phoneNumber.startsWith("0") || phoneNumber.startsWith("+")) -> {
                showError("Phone number must start with '0' or '+'.")
                false
            }
            !phoneNumber.drop(1).all { it.isDigit() } -> {
                showError("Phone number can only contain digits after the first character.")
                false
            }
            else -> true
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

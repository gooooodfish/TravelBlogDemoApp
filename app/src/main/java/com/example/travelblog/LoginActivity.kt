package com.example.travelblog

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.travelblog.databinding.ActivityLoginBinding
import com.google.android.material.textfield.TextInputLayout


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val preferences: BlogPreferences by lazy {
        BlogPreferences(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        if (preferences.isLoggedIn()) {
//            startMainActivity()
//            finish()
//            return
//        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener { onLoginClicked() }

        binding.textUsernameLayout.editText
            ?.addTextChangedListener(createTextWatcher(binding.textUsernameLayout))

        binding.textPasswordInput.editText
            ?.addTextChangedListener(createTextWatcher(binding.textPasswordInput))
    }

    private fun onLoginClicked() {
        val username: String = binding.textUsernameLayout.editText?.text.toString()
        val password: String = binding.textPasswordInput.editText?.text.toString()
        if (username.isEmpty()) {
            binding.textUsernameLayout.error = "Username must not be empty"
        } else if (password.isEmpty()) {
            binding.textPasswordInput.error = "Password must not be empty"
        } else if (username != "admin" || password != "admin") {
            showErrorDialog()
        } else {
            performLogin()
        }
    }

    private fun performLogin() {
        preferences.setLoggedIn(true)
        binding.textUsernameLayout.isEnabled = false
        binding.textPasswordInput.isEnabled = false
        binding.loginButton.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE

        Handler().postDelayed(
            {
                startMainActivity()
                finish()
            }, 2000
        )
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun showErrorDialog() {
        AlertDialog.Builder(this)
            .setTitle("Login Failed")
            .setMessage("Username or password is not correct. Please try again.")
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun createTextWatcher(textInput: TextInputLayout): TextWatcher? {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no need
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                textInput.error = null
            }

            override fun afterTextChanged(s: Editable?) {
                //no need
            }

        }
    }

}
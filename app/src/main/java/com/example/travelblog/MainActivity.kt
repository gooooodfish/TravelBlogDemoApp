package com.example.travelblog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.travelblog.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.mainTextView.text = "Hello haoyu"

    }
}
package com.example.atmbank

import android.content.Intent
import android.net.Network
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.atmbank.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var type: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root) //view binding

        //checking for network connection and going to next activity
        if (NetworkHelper.isNetworkConnected(this)) {
            binding.currentButton.setOnClickListener {
                type = "current"
                intent = Intent(this, ReadPinNo::class.java)
                intent.putExtra("type", type)
                startActivity(intent)
            }
            binding.savingsButton.setOnClickListener {
                type = "savings"
                intent = Intent(this, ReadPinNo::class.java)
                intent.putExtra("type", type)
                startActivity(intent)
            }
        } else {
            Toast.makeText(
                this,
                "Sorry! There Is No Internet Connection.Please Try Again Later",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
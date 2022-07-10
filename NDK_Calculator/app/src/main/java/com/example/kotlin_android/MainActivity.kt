package com.example.kotlin_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.kotlin_android.databinding.ActivityMainBinding
import com.example.kotlin_android.ndk.NdkCalculator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Example of a call to a native method
//        binding.sampleText.text = stringFromJNI()

        binding.btnNdkCalculator.setOnClickListener{
            var view = Intent(this, NdkCalculator::class.java)
            startActivity(view)
        }
    }

    /**
     * A native method that is implemented by the 'kotlin_android' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'kotlin_android' library on application startup.
        init {
            System.loadLibrary("kotlin_android")
        }
    }
}
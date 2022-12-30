package com.electricCars

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var btnCalculation: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViews()
        setupListeners()

    }

    private fun setupViews() {
        btnCalculation = findViewById(R.id.fab_calculation)
    }

    private fun setupListeners() {
        btnCalculation.setOnClickListener {
            startActivity(Intent(this, ActivityCalculation::class.java))
        }
    }

}
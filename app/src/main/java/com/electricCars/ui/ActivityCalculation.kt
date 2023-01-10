package com.electricCars.ui

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.electricCars.R
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.AsynchronousByteChannel

class ActivityCalculation : AppCompatActivity() {

    private lateinit var etDistance: EditText
    private lateinit var etAutonomy: EditText
    private lateinit var etPrice: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvTotalCost: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculation)

        setupViews()
        setupListeners()
        setupCachedResult()
    }

    private fun setupCachedResult() {
        val calculatedValue = getSharedPref()
        tvTotalCost.text = calculatedValue.toString()
    }

    private fun setupViews() {
        etDistance = findViewById(R.id.et_distance)
        etAutonomy = findViewById(R.id.et_autonomy)
        etPrice = findViewById(R.id.et_price_kwh)
        btnCalculate = findViewById(R.id.btn_calculate)
        tvTotalCost = findViewById(R.id.tv_totalCost)
    }

    private fun setupListeners() {
        btnCalculate.setOnClickListener {
            calculate()
        }
    }

    private fun calculate() {
        val distance = etDistance.text.toString().toFloat()
        val autonomy = etAutonomy.text.toString().toFloat()
        val priceKWh = etPrice.text.toString().toFloat()
        val result = (distance / autonomy * priceKWh)
        tvTotalCost.text = getString(R.string.costs, result)
        saveSharedPref(result)
    }

    private fun saveSharedPref(result: Float) {
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putFloat(getString(R.string.saved_calc), result)
            apply()
        }
    }

    private fun getSharedPref(): Float {
        val sharedPref = getPreferences(Context.MODE_PRIVATE)
        return sharedPref.getFloat(getString(R.string.saved_calc), 0.0f)

    }

}
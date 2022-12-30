package com.electricCars

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

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
        tvTotalCost.text = getString(R.string.costs, (distance / autonomy * priceKWh))
    }

}
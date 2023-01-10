package com.electricCars.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.electricCars.R
import com.electricCars.domain.Car

class CarAdapter(private val cars: List<Car>) : RecyclerView.Adapter<CarAdapter.ViewHolder>() {

    var carItemListener: (Car) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.car_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.name.text = cars[position].name
        holder.price.text = cars[position].price
        holder.battery.text = cars[position].battery
        holder.power.text = cars[position].power
        holder.recharge.text = cars[position].recharge
        holder.favorite.setOnClickListener {
            val car = cars[position]
            carItemListener(car)
            setupFavorite(car, holder)

        }
    }

    private fun setupFavorite(
        car: Car,
        holder: ViewHolder
    ) {
        car.isFavorite = !car.isFavorite
        if (car.isFavorite) holder.favorite.setImageResource(R.drawable.ic_star_selected)
        else holder.favorite.setImageResource(R.drawable.ic_star)
    }

    override fun getItemCount() = cars.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val name: TextView
        val price: TextView
        val battery: TextView
        val power: TextView
        val recharge: TextView
        val favorite: ImageView
        init{
            view.apply {
                name = findViewById(R.id.tv_car_name)
                price = findViewById(R.id.tv_price_value)
                battery = findViewById(R.id.tv_battery_value)
                power = findViewById(R.id.tv_power_value)
                recharge = findViewById(R.id.tv_recharge_time)
                favorite = findViewById(R.id.iv_favorite)
            }
        }
    }

}
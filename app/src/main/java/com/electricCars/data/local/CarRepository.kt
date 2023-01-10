package com.electricCars.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_BATTERY
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_NAME
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_POWER
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_PRICE
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_RECHARGE
import com.electricCars.data.local.CarsContract.CarEntry.COLUMN_URL_PHOTO
import com.electricCars.domain.Car

class CarRepository(private val context: Context) {

    fun save(car: Car) : Boolean {
        var isSaved = false
        try {
            val dbHelper = CarsDbHelper(context)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(COLUMN_NAME, car.name)
                put(COLUMN_PRICE, car.price)
                put(COLUMN_BATTERY, car.battery)
                put(COLUMN_POWER, car.power)
                put(COLUMN_RECHARGE, car.recharge)
                put(COLUMN_URL_PHOTO, car.urlPhoto)
            }

            val inserted = db?.insert(CarsContract.CarEntry.TABLE_NAME, null, values)

            if (inserted != null) isSaved = true

        } catch (_: Exception) { }

        return isSaved
        }

    fun findCarById(id: Int) {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase

        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME,
            COLUMN_PRICE,
            COLUMN_BATTERY,
            COLUMN_POWER,
            COLUMN_RECHARGE,
            COLUMN_URL_PHOTO
        )

        val filter = "${BaseColumns._ID} = ?"
        val filterValues = arrayOf(id.toString())

        val cursor = db.query(
            CarsContract.CarEntry.TABLE_NAME,
            columns,
            filter,
            filterValues,
            null,
            null,
            null
        )
    }

}
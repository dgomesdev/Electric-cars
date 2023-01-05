package com.electricCars.data

import com.electricCars.domain.Car
import retrofit2.Call
import retrofit2.http.GET

interface CarsApi {

    @GET("cars.json")
    fun getAllCArs() : Call<List<Car>>


}
package com.electricCars.data

import com.electricCars.domain.Car

object CarFactory {

    val list = listOf(
        Car(
            id = 1,
            name = "NAME",
            price = "PRICE",
            power = "POWER",
            battery = "BATTERY",
            recharge = "RECHARGE",
            urlPhoto = "www.google.com"
        ),
        Car(
            id = 2,
            name = "NAME",
            price = "PRICE",
            power = "POWER",
            battery = "BATTERY",
            recharge = "RECHARGE",
            urlPhoto = "www.google.com"
        )
    )

}
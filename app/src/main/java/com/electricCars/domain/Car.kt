package com.electricCars.domain

data class Car (
    val id: Int,
    val name: String,
    val price: String,
    val battery: String,
    val power: String,
    val recharge: String,
    val urlPhoto: String,
    var isFavorite: Boolean
        )
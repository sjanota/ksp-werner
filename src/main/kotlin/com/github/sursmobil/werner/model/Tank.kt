package com.github.sursmobil.werner.model

data class Tank(
        val name: String,
        val vol: Double,
        private val baseCost: Int,
        private val fuelType: FuelType
) {
    val mass: Double = fuelType.tankMass(this)
    val fuelMass = vol * fuelType.density

    val cost: Int = baseCost
}
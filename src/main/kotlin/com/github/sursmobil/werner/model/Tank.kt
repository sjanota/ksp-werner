package com.github.sursmobil.werner.model

data class Tank(
        val name: String,
        val vol: Double,
        val baseCost: Int,
        private val fuelType: FuelType
) {
    val mass: Double = fuelType.tankMass(this)
    val fuelMass = vol * fuelType.density

    companion object {
        val Empty = Tank("Empty", 0.0, 0, FuelType.None)
    }

    val cost: Int = baseCost
}
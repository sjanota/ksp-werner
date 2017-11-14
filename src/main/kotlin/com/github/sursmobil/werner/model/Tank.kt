package com.github.sursmobil.werner.model

data class Tank(
        val name: String,
        val vol: Double,
        private val fuelType: FuelType
) {
    val mass: Double = fuelType.tankMass(this)
    val fuelMass = vol * fuelType.density
}
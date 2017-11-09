package com.github.sursmobil.werner.model

data class Tank(
        val name: String,
        val vol: Double,
        val fuelType: FuelType
) {
    val mass: Double = fuelType.tankMass(this)
    val fuelMass: Double = fuelType.fuelMass(vol)
}
package com.github.sursmobil.werner.model

data class Engine(
        val name: String,
        val cost: Int,
        val mass: Double,
        val fuelVolUsage: Double,
        val includedFuel: Double = 0.0,
        val tankFamily: TankFamily,
        val fuelType: FuelType,
        val thrust: Thrust
) {
    val fuelMassUsage = fuelVolUsage * fuelType.density

    companion object {
        val None = Engine("None", 0, 0.0, 0.0, 0.0, TankFamily.None, FuelType.None, Thrust.None)
    }
}



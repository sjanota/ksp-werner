package com.github.sursmobil.werner.model

/**
 * Created by sj on 08/11/2017.
 */
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
    val fuelMassUsage = fuelType.fuelMass(fuelVolUsage)
}

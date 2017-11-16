package com.github.sursmobil.werner.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(`as` = BaseEngine::class)
abstract class Engine(
        private val name: String,
        val cost: Int,
        val mass: Double,
        val fuelVolUsage: Double,
        val includedFuel: Double,
        val tankFamily: TankFamily,
        val fuelType: FuelType,
        val thrust: Thrust,
        val size: Double
) {
    val fuelMassUsage = fuelVolUsage * fuelType.density

    abstract fun morph(): List<Engine>
    override fun toString(): String =
            "Engine(name='$name')"

    object None : Engine("None", 0, 0.0, 0.0, 0.0, TankFamily.None, FuelType.None, Thrust.None, 0.0) {
        override fun morph(): List<Engine> = Registry.engines
    }

    object Registry {
        internal val engines = mutableListOf<Engine>()

    }

    val fuelMass: Double = includedFuel * fuelType.density
}

class BaseEngine(name: String, cost: Int, mass: Double, fuelVolUsage: Double, includedFuel: Double = 0.0, tankFamily: TankFamily, fuelType: FuelType, thrust: Thrust, size: Double) : Engine(name, cost, mass, fuelVolUsage, includedFuel, tankFamily, fuelType, thrust, size) {
    init {
        Registry.engines.add(this)
    }

    override fun morph(): List<Engine> = emptyList()
}



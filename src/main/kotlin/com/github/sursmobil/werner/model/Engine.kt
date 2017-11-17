package com.github.sursmobil.werner.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(`as` = BaseEngine::class)
abstract class Engine(
        private val name: String,
        val cost: Int,
        val mass: Double,
        val fuelVolUsage: Double,
        val includedFuel: Double,
        val fuelType: FuelType,
        val thrust: Thrust,
        override val size: Double
) : MountSurface {
    val fuelMassUsage = fuelVolUsage * fuelType.density

    abstract fun morph(): List<Engine>
    override fun toString(): String =
            "Engine(name='$name')"

    object None : Engine("None", 0, 0.0, 0.0, 0.0, FuelType.None, Thrust.None, 0.0) {
        override fun morph(): List<Engine> = Registry.engines
    }

    object Registry {
        internal val engines = mutableListOf<Engine>()

    }

    val fuelMass: Double = includedFuel * fuelType.density
}

class BaseEngine(name: String, cost: Int, mass: Double, fuelVolUsage: Double, includedFuel: Double = 0.0, fuelType: FuelType, thrust: Thrust, size: Double) : Engine(name, cost, mass, fuelVolUsage, includedFuel, fuelType, thrust, size) {
    init {
        Registry.engines.add(this)
    }

    override fun morph(): List<Engine> = emptyList()
}



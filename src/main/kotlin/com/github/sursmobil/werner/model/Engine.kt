package com.github.sursmobil.werner.model

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

@JsonDeserialize(`as` = Engine.BaseEngine::class)
abstract class Engine(
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

    abstract fun morph(): List<Engine>

    object None : Engine("None", 0, 0.0, 0.0, 0.0, TankFamily.None, FuelType.None, Thrust.None) {
        override fun morph(): List<Engine> = Registry.engines
    }

    class BaseEngine(name: String, cost: Int, mass: Double, fuelVolUsage: Double, includedFuel: Double, tankFamily: TankFamily, fuelType: FuelType, thrust: Thrust) : Engine(name, cost, mass, fuelVolUsage, includedFuel, tankFamily, fuelType, thrust) {
        init {
            Registry.engines.add(this)
        }

        override fun morph(): List<Engine> = emptyList()
    }

    object Registry {
        internal val engines = mutableListOf<Engine>()

        private val indexByName
            get() = engines.associateBy { it.name }

        fun byName(name: String) = indexByName[name] ?: throw Exception("Engine '$name' not found")
    }
}



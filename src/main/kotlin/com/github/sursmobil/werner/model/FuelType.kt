package com.github.sursmobil.werner.model

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.github.sursmobil.werner.data.tanks.Tank

@JsonDeserialize(using = FuelType.Deserializer::class)
interface FuelType {
    val density: Double
    fun tankMass(tank: Tank): Double
    fun cost(vol: Double): Int

    class Deserializer : StdDeserializer<FuelType>(FuelType::class.java) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): FuelType = when(p?.valueAsString) {
            "liquid" -> LiquidFuel
            "solid" -> SolidFuel
            else -> throw Exception("Unsupported fuel type: ${p?.valueAsString}")
        }
    }

    object LiquidFuel : FuelType {
        private const val FUEL_DENSITY: Double = 0.005
        private const val FUEL_COST: Double = 0.8
        private const val OXIDIZER_COST: Double = 0.18
        private const val OXIDIZER_DENSITY: Double = 0.005
        private const val OXIDIZER_TO_FUEL_RATIO: Double = 11.0/9.0
        private const val TANK_MASS_FACTOR: Double = 8.0

        override val density: Double = FUEL_DENSITY + OXIDIZER_DENSITY * OXIDIZER_TO_FUEL_RATIO
        override fun tankMass(tank: Tank): Double = tank.vol * density / TANK_MASS_FACTOR
        override fun cost(vol: Double): Int = (vol * (FUEL_COST + OXIDIZER_COST * OXIDIZER_TO_FUEL_RATIO)).toInt()
        override fun toString() = "Liquid"
    }

    object SolidFuel : FuelType {
        private const val DENSITY: Double = 0.0075

        override val density: Double = DENSITY
        override fun cost(vol: Double): Int {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
        override fun tankMass(tank: Tank): Double {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun toString() = "Solid"
    }

    object None : FuelType {
        override val density: Double = 0.0
        override fun cost(vol: Double): Int = 0
        override fun tankMass(tank: Tank): Double = 0.0
        override fun toString() = "None"
    }
}


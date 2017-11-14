package com.github.sursmobil.werner.model

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

@JsonDeserialize(using = FuelType.Deserializer::class)
interface FuelType {
    val density: Double
    fun tankMass(tank: Tank): Double

    class Deserializer : StdDeserializer<FuelType>(FuelType::class.java) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): FuelType = when(p?.valueAsString) {
            "liquid" -> LiquidFuel
            "solid" -> SolidFuel
            else -> throw Exception("Unsupported fuel type: ${p?.valueAsString}")
        }
    }

    object LiquidFuel : FuelType {

        private const val FUEL_DENSITY: Double = 0.005
        private const val OXIDIZER_DENSITY: Double = 0.005
        private const val OXIDIZER_TO_FUEL_RATIO: Double = 11.0/9.0
        private const val TANK_MASS_FACTOR: Double = 8.0

        override fun tankMass(tank: Tank): Double =
                tank.vol * density / TANK_MASS_FACTOR

        override val density: Double = FUEL_DENSITY + OXIDIZER_DENSITY * OXIDIZER_TO_FUEL_RATIO
    }

    object SolidFuel : FuelType {
        private const val DENSITY: Double = 0.0075

        override val density: Double = DENSITY

        override fun tankMass(tank: Tank): Double {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    object None : FuelType {
        override val density: Double = 0.0

        override fun tankMass(tank: Tank): Double {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

    }
}


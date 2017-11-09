package com.github.sursmobil.werner.model

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

@JsonDeserialize(using = FuelType.Deserializer::class)
interface FuelType {
    fun fuelMass(fuelVol: Double): Double
    fun tankMass(tank: Tank): Double

    class Deserializer : StdDeserializer<FuelType>(FuelType::class.java) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): FuelType = when(p?.valueAsString) {
            "liquid" -> LiquidFuel
            "solid" -> SolidFuel
            else -> throw Exception("Unsupported fuel type: ${p?.valueAsString}")
        }
    }

    object LiquidFuel : FuelType {

        const val FUEL_DENSITY: Double = 0.005
        const val OXIDIZER_DENSITY: Double = 0.005
        const val OXIDIZER_TO_FUEL_RATIO: Double = 9.0/11.0
        const val TANK_MASS_FACTOR: Double = 8.0

        override fun tankMass(tank: Tank): Double =
                fuelMass(tank.vol) / TANK_MASS_FACTOR

        override fun fuelMass(fuelVol: Double): Double =
                fuelVol * (FUEL_DENSITY + OXIDIZER_DENSITY * OXIDIZER_TO_FUEL_RATIO)
    }

    object SolidFuel : FuelType {
        const val DENSITY: Double = 0.0075

        override fun fuelMass(fuelVol: Double): Double =
                fuelVol * DENSITY

        override fun tankMass(tank: Tank): Double {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }
}


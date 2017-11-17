package com.github.sursmobil.werner.model.tanks

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.sursmobil.werner.model.FuelType

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = TankFactory.Scaled::class, name = "scaled"),
        JsonSubTypes.Type(value = TankFactory.Static::class, name = "static")
)
internal interface TankFactory {
    fun createTanks(): List<Tank>

    class Scaled(
            private val name: String,
            private val fuelType: FuelType,
            private val factors: Map<Double, Int>,
            private val nominalVol: Double,
            private val mount: TankMount
    ) : TankFactory {
        override fun createTanks(): List<Tank> = factors.map { factor ->
            val tankVol = nominalVol * factor.key
            val tankName = "$name-$tankVol"
            Tank(tankName, tankVol, factor.value, fuelType, mount)
        }
    }

    class Static(
            private val name: String,
            private val vol: Double,
            private val cost: Int,
            private val fuelType: FuelType,
            private val mount: TankMount
    ) : TankFactory {
        override fun createTanks(): List<Tank> = listOf(Tank(name, vol, cost, fuelType, mount))
    }
}
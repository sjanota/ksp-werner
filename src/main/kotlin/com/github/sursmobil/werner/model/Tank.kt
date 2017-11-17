package com.github.sursmobil.werner.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize


data class Tank(
        val name: String,
        val vol: Double,
        val cost: Int,
        val fuelType: FuelType,
        private val mount: TankMount
) : Mountable by mount {
    val mass: Double = fuelType.tankMass(this)
    val fuelMass = vol * fuelType.density
}

class TanksRegistry(
        @JsonDeserialize(using = Deserializer::class)
        val tanks: List<Tank>
) {
    private class Deserializer : JsonDeserializer<List<Tank>>() {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): List<Tank> {
            return p!!.readValueAs<List<Factory>>(object : TypeReference<List<Factory>>() {})
                    .asSequence().toList()
                    .flatMap { it.createTanks() }
        }
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
    @JsonSubTypes(
            JsonSubTypes.Type(value = Factory.Scaled::class, name = "scaled"),
            JsonSubTypes.Type(value = Factory.Static::class, name = "static")
    )
    private interface Factory {
        fun createTanks(): List<Tank>

        class Scaled(
                private val name: String,
                private val fuelType: FuelType,
                private val factors: Map<Double, Int>,
                private val nominalVol: Double,
                private val mount: TankMount
        ) : Factory {
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
        ) : Factory {
            override fun createTanks(): List<Tank> = listOf(Tank(name, vol, cost, fuelType, mount))
        }
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
        JsonSubTypes.Type(value = RadialMount::class, name = "radial"),
        JsonSubTypes.Type(value = MainMount::class, name = "main")
)
interface TankMount : Mountable
class RadialMount : TankMount {
    override fun canBeMounted(surface: MountSurface): Boolean = true
}

class MainMount(val size: Double) : TankMount {
    override fun canBeMounted(surface: MountSurface): Boolean = size >= surface.size
}

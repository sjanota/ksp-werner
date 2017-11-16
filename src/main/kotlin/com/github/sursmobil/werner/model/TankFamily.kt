package com.github.sursmobil.werner.model

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

@JsonDeserialize(using = Deserializer::class)
data class TankFamily(
        val name: String,
        val tanks: List<Tank>
) {
    val smallest: Tank? = tanks.minBy { it.vol }
    val orderedTanks = tanks.sortedBy { -it.vol }

    companion object {
        val None = TankFamily("None", emptyList())
    }
}

private class Deserializer : StdDeserializer<TankFamily>(TankFamily::class.java) {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): TankFamily {
        if(p!!.currentToken.isScalarValue) {
            val name = p.readValueAs(String::class.java)
            return Registry.getFamily(name)
        }
        val factory = p.readValueAs(Factory::class.java)
        return factory.createFamily()
    }
}

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
//        JsonSubTypes.Type(value = Factory.Scaled::class, name = "scaled"),
        JsonSubTypes.Type(value = Factory.Static::class, name = "static"),
        JsonSubTypes.Type(value = Factory.Combined::class, name = "combined")
)
private abstract class Factory(val name: String) {
    protected abstract fun createTanks(): List<Tank>

    fun createFamily() : TankFamily {
        val tanks = createTanks()
        val family = TankFamily(name, tanks)
        Registry.addFamily(family)
        return family
    }

//    class Scaled(name: String, private val fuelType: FuelType, private val factors: Map<Double, Int>, private val nominalVol: Double) : Factory(name) {
//        override fun createTanks(): List<Tank> = factors.map { factor ->
//            val tankVol = nominalVol * factor.key
//            val tankName = "$name-$tankVol"
//            Tank(tankName, tankVol, factor.value, fuelType, mount)
//        }
//    }

    class Static(name: String, private val tanks: List<Tank>) : Factory(name) {
        override fun createTanks(): List<Tank> = tanks
    }

    class Combined(name: String, private val families: List<TankFamily>): Factory(name) {
        override fun createTanks(): List<Tank> = families.flatMap { it.tanks }
    }
}

private object Registry {
    val registry = mutableMapOf<String, TankFamily>()

    fun getFamily(name: String) = registry[name] ?: throw Exception("Tank family '$name' not found!")

    fun addFamily(family: TankFamily) {
        registry.put(family.name, family)
    }
}

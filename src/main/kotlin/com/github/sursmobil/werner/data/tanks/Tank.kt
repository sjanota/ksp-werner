package com.github.sursmobil.werner.data.tanks

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.github.sursmobil.werner.model.FuelType
import com.github.sursmobil.werner.model.MountSurface
import com.github.sursmobil.werner.model.Mountable


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

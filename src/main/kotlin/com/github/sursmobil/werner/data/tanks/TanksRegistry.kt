package com.github.sursmobil.werner.data.tanks

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.sursmobil.werner.model.Engine
import com.github.sursmobil.werner.model.MountSurface

@JsonDeserialize(`as` = BaseTanksRegistry::class)
interface TanksRegistry {
    val size: Int

    fun onlyMountable(surface: MountSurface): TanksRegistry
    fun onlySameFuel(engine: Engine): TanksRegistry
    fun volDescending(): List<Tank>
    fun volSmallest(): Tank
}

class BaseTanksRegistry(
        @JsonDeserialize(using = TankListDeserializer::class)
        val tanks: List<Tank>
) : TanksRegistry {
    override val size = tanks.size

    override fun onlyMountable(surface: MountSurface): TanksRegistry =
            BaseTanksRegistry(tanks.filter { it.canBeMounted(surface) })

    override fun onlySameFuel(engine: Engine): TanksRegistry =
            BaseTanksRegistry(tanks.filter { it.fuelType == engine.fuelType })

    override fun volDescending(): List<Tank> =
            tanks.sortedBy { -it.vol }

    override fun volSmallest(): Tank =
            tanks.minBy { it.vol }!!
}


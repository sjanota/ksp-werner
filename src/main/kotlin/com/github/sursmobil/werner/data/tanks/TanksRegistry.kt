package com.github.sursmobil.werner.data.tanks

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.sursmobil.werner.model.Engine
import com.github.sursmobil.werner.model.MountSurface

@JsonDeserialize(`as` = BaseTanksRegistry::class, using = TankRegistryDeserializer::class)
interface TanksRegistry : Set<Tank> {
    fun onlyMountable(surface: MountSurface): TanksRegistry
    fun onlySameFuel(engine: Engine): TanksRegistry
    fun volDescending(): List<Tank>
    fun volSmallest(): Tank

    operator fun plus(other: TanksRegistry): TanksRegistry
}

class BaseTanksRegistry(
        val tanks: Set<Tank>
) : TanksRegistry, Set<Tank> by tanks {
    override fun onlyMountable(surface: MountSurface): TanksRegistry =
            BaseTanksRegistry(tanks.filter { it.canBeMounted(surface) }.toSet())

    override fun onlySameFuel(engine: Engine): TanksRegistry =
            BaseTanksRegistry(tanks.filter { it.fuelType == engine.fuelType }.toSet())

    override fun volDescending(): List<Tank> =
            tanks.sortedBy { -it.vol }

    override fun volSmallest(): Tank =
            tanks.minBy { it.vol }!!

    override operator fun plus(other: TanksRegistry): TanksRegistry =
            BaseTanksRegistry(union(other))
}


package com.github.sursmobil.werner.model

import java.util.*

data class Tanks(
        val list: List<Tank>
) {
    val mass: Double = list.sumByDouble { it.mass }
    val fuelMass: Double = list.sumByDouble { it.fuelMass }
    val vol: Double = list.sumByDouble { it.vol }

    companion object {
        fun empty() = Tanks(emptyList())
    }

    fun add(tank: Tank, tankAmount: Int): Tanks {
        return Tanks(list + Collections.nCopies(tankAmount, tank) )
    }
}
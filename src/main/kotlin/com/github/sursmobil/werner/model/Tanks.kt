package com.github.sursmobil.werner.model

import java.util.*

data class Tanks(
        val list: List<Tank>
) {
    val mass: Double = list.sumByDouble { it.mass }
    val fuelMass: Double = list.sumByDouble { it.fuelMass }
    val vol: Double = list.sumByDouble { it.vol }
    val counts: Map<String, Int> = list.groupBy { it.name }.mapValues { it.value.size }

    companion object {
        fun empty() = Tanks(emptyList())
    }

    fun add(tank: Tank, tankAmount: Int): Tanks = Tanks(list + Collections.nCopies(tankAmount, tank) )
    val cost: Int = list.sumBy { it.cost }
    val rawCost: Int = list.sumBy { it.baseCost }
}
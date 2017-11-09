package com.github.sursmobil.werner.model

data class Tanks(
        val counts: Map<Tank, Int>
) {
    val mass: Double = counts.map { it.key.mass * it.value }.sum()
    val fuelMass: Double = counts.map { it.key.fuelMass * it.value }.sum()
    val vol: Double = counts.map { it.key.vol * it.value }.sum()

    companion object {
        fun empty() = Tanks(emptyMap())
    }
}
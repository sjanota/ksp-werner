package com.github.sursmobil.werner.model

data class Thrust(
        val asl: Double,
        val vac: Double
) {
    companion object {
        val None = Thrust(0.0, 0.0)
    }
}
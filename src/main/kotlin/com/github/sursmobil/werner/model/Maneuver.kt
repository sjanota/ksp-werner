package com.github.sursmobil.werner.model

/**
 * Created by sj on 08/11/2017.
 */
data class Maneuver(
        val planet: Planet,
        val dV: Double,
        val env: Env
)

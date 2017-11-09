package com.github.sursmobil.werner.model

/**
 * Created by sj on 08/11/2017.
 */
data class Stage(
        val engine: Engine,
        val payload: Payload,
        val tanks: Tanks = Tanks.empty()
) {
    val rawMass = engine.mass + payload.mass + tanks.mass
    val fuelMass = tanks.fuelMass
    val fuelVol = tanks.vol
    val totalMass = rawMass + fuelMass
}


package com.github.sursmobil.werner.model

data class Stage(
        val engine: Engine,
        private val payload: Payload,
        val tanks: Tanks = Tanks.empty()
) {
    val rawMass = engine.mass + payload.mass + tanks.mass
    private val fuelMass = tanks.fuelMass
    val fuelVol = tanks.vol
    val totalMass = rawMass + fuelMass
    fun setTanks(tanks: Tanks): Stage = Stage(engine, payload, tanks)
}


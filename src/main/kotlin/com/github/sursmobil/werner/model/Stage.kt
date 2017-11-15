package com.github.sursmobil.werner.model

data class Stage(
        val engine: Engine,
        val payload: Payload,
        val tanks: Tanks = Tanks.empty()
) : Payload {
    override val mass: Double
        get() = totalMass
    val rawMass = engine.mass + payload.mass + tanks.mass - engine.fuelMass
    val fuelMass = tanks.fuelMass + engine.fuelMass
    val fuelVol = tanks.vol + engine.includedFuel
    val totalMass = rawMass + fuelMass
    fun setTanks(tanks: Tanks): Stage = Stage(engine, payload, tanks)

    override fun toString(): String = "Stage(engine=$engine, payload=$payload, tanks=${tanks.counts}, vol=$fuelVol, rawCost=$rawCost, cost=$cost)"

    override val cost: Int = engine.cost + payload.cost + tanks.cost
    private val rawCost: Int = engine.cost + payload.cost + tanks.rawCost

    companion object {
        val None = Stage(Engine.None, Payload.create(0.0, 0))
    }

}


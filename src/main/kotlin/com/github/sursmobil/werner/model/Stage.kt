package com.github.sursmobil.werner.model

class Stage(
        val engine: Engine,
        val payload: Payload,
        private val tanks: Tanks = Tanks.Empty
) : Payload, MassWithThrust {
    val rawMass = engine.mass + payload.mass + tanks.mass - engine.fuelMass
    val fuelMass = tanks.fuelMass + engine.fuelMass
    val fuelVol = tanks.vol + engine.includedFuel
    val burnTime = fuelVol / engine.fuelVolUsage

    override val mass: Double = rawMass + fuelMass
    override val cost: Int = engine.cost + payload.cost + tanks.cost
    override val thrust: Thrust = engine.thrust
    override fun toString(): String = "Stage(engine=$engine, payload=$payload, tanks=${tanks.counts}, vol=$fuelVol, cost=$cost)"

    fun setTanks(tanks: Tanks): Stage = Stage(engine, payload, tanks)

    companion object {
        val Empty = Stage(Engine.None, Payload.Empty)
    }
}


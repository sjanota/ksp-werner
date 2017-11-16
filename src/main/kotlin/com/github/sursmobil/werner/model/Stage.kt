package com.github.sursmobil.werner.model

interface Stage : Payload, MassWithThrust {
    val engine: Engine
    val payload: Payload
    val rawMass: Double
    val burnTime: Double
    val fuelVol: Double

    fun addManeuver(maneuver: Maneuver): ExecutedStage
    fun nextStage(additionalPayload: Payload): Stage
    fun setTanks(tanks: Tanks): Stage
    fun setEngine(engine: Engine): Stage

    companion object {
        val Empty = BaseStage()
    }
}

class BaseStage (
        override val engine: Engine = Engine.None,
        override val payload: Payload = Payload.Empty,
        private val tanks: Tanks = Tanks.Empty
) : Stage {
    private val fuelMass = tanks.fuelMass + engine.fuelMass

    override val rawMass = engine.mass + payload.mass + tanks.mass - engine.fuelMass
    override val fuelVol = tanks.vol + engine.includedFuel
    override val burnTime = fuelVol / engine.fuelVolUsage
    override val mass: Double = rawMass + fuelMass
    override val cost: Int = engine.cost + payload.cost + tanks.cost
    override val thrust: Thrust = engine.thrust
    override fun toString(): String =
            "Stage(engine=$engine, payload=$payload, tanks=${tanks.counts}, vol=$fuelVol, cost=$cost)"

    override fun setEngine(engine: Engine): Stage = BaseStage(engine, payload, tanks)
    override fun setTanks(tanks: Tanks): Stage = BaseStage(engine, payload, tanks)
    override fun addManeuver(maneuver: Maneuver) = ExecutedStage(this, listOf(maneuver))
    override fun nextStage(additionalPayload: Payload) = BaseStage(payload = this + additionalPayload)
}

class ExecutedStage(
        private val stage: Stage,
        private val maneuvers: List<Maneuver>,
        private val usedFuel: Double = 0.0
) : Stage by stage {
    private val restrictions = maneuvers.flatMap { it.restrictions }

    override fun setTanks(tanks: Tanks): ExecutedStage = ExecutedStage(stage.setTanks(tanks), maneuvers, usedFuel)
    override fun addManeuver(maneuver: Maneuver): ExecutedStage = ExecutedStage(stage, maneuvers + maneuver, usedFuel)
    override fun toString(): String = stage.toString()

    fun setUsedFuel(usedFuel: Double): ExecutedStage = ExecutedStage(stage, maneuvers, usedFuel)
    fun matchesRestrictions():Boolean = stage.fuelVol >= usedFuel && restrictions.all { it(this) }
}
package com.github.sursmobil.werner.calc

import com.github.sursmobil.werner.model.*


val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()

class StageCalculator(
        val stage: Stage,
        private val restrictions: Collection<StageRestriction> = emptyList()
) {
    private fun thrust(maneuver: Maneuver) = maneuver.env.thrust(stage.engine.thrust)
    private fun weight(maneuver: Maneuver) = maneuver.planet.gravity * stage.totalMass
    private fun canExecute(maneuver: Maneuver) = (restrictions + maneuverRestrictions(maneuver)).all { it(this) }

    private fun maneuverRestrictions(maneuver: Maneuver): Collection<StageRestriction> =
            maneuver.restrictions + haveEnoughFuel(maneuver)

    private fun haveEnoughFuel(maneuver: Maneuver): StageRestriction = { stage ->
        stage.deltaVForFuelVol(maneuver, stage.stage.fuelVol) >= maneuver.dV
    }

    private fun burnTime(fuelVol: Double = stage.fuelVol) = fuelVol / stage.engine.fuelVolUsage

    fun currentTWR(maneuver: Maneuver): Double = thrust(maneuver) / weight(maneuver)

    private fun deltaVForFuelVol(maneuver: Maneuver, fuelVol: Double): Double {
        val burnTime = burnTime(fuelVol)
        val fuelMass = fuelVol * stage.engine.fuelType.density
        val totalMass = stage.rawMass + fuelMass
        val thrust = thrust(maneuver)
        val fuelMassUsage = stage.engine.fuelMassUsage

        // dV = integrate thrust / (totalMass - t * fuelMassUsage) for t=0 to burnTime
        return -thrust * Math.log(1 - burnTime * fuelMassUsage / totalMass) / fuelMassUsage
    }

    fun burnTimeToDeltaV(maneuver: Maneuver): Double {
        val c1 = - maneuver.dV * stage.engine.fuelMassUsage / thrust(maneuver)
        val c2 = 1 - Math.exp(c1)

        return c2 * stage.totalMass / stage.engine.fuelMassUsage
    }

    private fun requiredFuel(maneuver: Maneuver): Double {
        // deltaVForFuelVol(Vf) = maneuver.dV , find Vf

        val c1 = - maneuver.dV * stage.engine.fuelMassUsage / thrust(maneuver)
        val c2 = 1 - Math.exp(c1)
        val c3 = stage.rawMass * c2
        val c4 = stage.engine.fuelMassUsage / stage.engine.fuelVolUsage
        val c5 = c2 * stage.engine.fuelType.density
        return c3 / (c4 - c5)
    }

    private fun fillTanks(fuelToFill: Double, tolerance: Double): Tanks? {
        val tanks = fillTanksNoExceed(fuelToFill, tolerance, Tanks.empty(), stage.engine.tankFamily.tanks)
        val hasEnough = fuelToFill <= tanks.vol
        return if (hasEnough) tanks else addSmallestTank(tanks)
    }

    private fun fillTanksNoExceed(fuelToFill: Double, tolerance: Double, tanks: Tanks, available: List<Tank>): Tanks {
        if (available.isEmpty())
            return tanks

        val tank = available.head
        val tankAmount = Math.round(Math.max(0.0, fuelToFill / tank.vol) - (0.5 - tolerance))
        val newTanks = tanks.add(tank, tankAmount.toInt())
        val newFuelToFill = fuelToFill - tank.vol * tankAmount
        return fillTanksNoExceed(newFuelToFill, tolerance, newTanks, available.tail)
    }

    private fun addSmallestTank(tanks: Tanks): Tanks? {
        return if (stage.engine.tankFamily.smallest != null) {
            tanks.add(stage.engine.tankFamily.smallest, 1)
        } else {
            null
        }
    }

    fun addManeuver(maneuver: Maneuver): List<StageCalculator> {
        return if (canExecute(maneuver)) {
            listOf(includeManeuver(maneuver))
        } else {
            val filled = calculateFuelTanks(maneuver)
            if (filled.canExecute(maneuver)) {
                listOf(filled.includeManeuver(maneuver))
            } else {
                stage.engine.morph()
                        .map { Stage(it, stage.payload) }
                        .map { StageCalculator(it, restrictions) }
                        .flatMap { it.addManeuver(maneuver) }
            }
        }
    }

    private fun includeManeuver(maneuver: Maneuver): StageCalculator =
            StageCalculator(stage, restrictions + maneuverRestrictions(maneuver))

    private fun calculateFuelTanks(maneuver: Maneuver, tolerance: Double = 0.1): StageCalculator {
        val requiredFuel = requiredFuel(maneuver)
        if (stage.tanks.vol >= requiredFuel)
            return this

        val tanks = fillTanks(requiredFuel, tolerance)
        return if (tanks == null) this else StageCalculator(stage.setTanks(tanks))
                .calculateFuelTanks(maneuver, tolerance)
    }
}
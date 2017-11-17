package com.github.sursmobil.werner

import com.github.sursmobil.werner.model.*
import com.github.sursmobil.werner.data.tanks.Tank
import com.github.sursmobil.werner.data.tanks.TanksRegistry

class StageCalculator (
        private val tanksReg: TanksRegistry,
        private val stage: Stage
) {
    fun addManeuver(maneuver: Maneuver): List<Stage> =
            ManeuverExecutor(tanksReg, stage, maneuver).adapt()

    fun burnTimeToDeltaV(maneuver: Maneuver): Double {
        val c1 = -maneuver.dV * stage.engine.fuelMassUsage / maneuver.env.thrust(stage)
        val c2 = 1 - Math.exp(c1)
        return c2 * stage.mass / stage.engine.fuelMassUsage
    }
}

private class ManeuverExecutor(
        private val tanksReg: TanksRegistry,
        private val stage: Stage,
        private val maneuver: Maneuver
) {
    private val executed = stage.addManeuver(maneuver)
    private val thrust = maneuver.env.thrust(executed)
    private val onlyMountableTanks = tanksReg.onlyMountable(stage.engine)
    private val orderedTanks = onlyMountableTanks.volDescending()
    private val smallestTank = onlyMountableTanks.volSmallest()

    private val requiredFuel: Double
        get() {
            val c1 = -maneuver.dV * executed.engine.fuelMassUsage / thrust
            val c2 = 1 - Math.exp(c1)
            val c3 = executed.rawMass * c2
            val c4 = executed.engine.fuelMassUsage / executed.engine.fuelVolUsage
            val c5 = c2 * executed.engine.fuelType.density
            return c3 / (c4 - c5)
        }

    private fun calculateFuelTanks(tolerance: Double = 0.1, maxFuel: Double = 10000000.0): ExecutedStage {
        if (executed.fuelVol >= requiredFuel || requiredFuel > maxFuel)
            return executed.setUsedFuel(requiredFuel)

        val tanks = fillTanks(requiredFuel - executed.engine.includedFuel, tolerance)
        return recalculateFuelIfPossible(tanks, tolerance, maxFuel)
    }

    private fun recalculateFuelIfPossible(tanks: Tanks?, tolerance: Double, maxFuel: Double): ExecutedStage {
        return if (tanks == null) {
            executed.setUsedFuel(requiredFuel)
        } else {
            ManeuverExecutor(tanksReg, stage.setTanks(tanks), maneuver)
                    .calculateFuelTanks(tolerance, maxFuel)
        }
    }

    private fun fillTanks(fuelToFill: Double, tolerance: Double): Tanks? {
        val tanks = fillTanksNoExceed(fuelToFill, tolerance, Tanks.Empty, orderedTanks)
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
        return if (onlyMountableTanks.size != 0) {
            tanks.add(smallestTank, 1)
        } else {
            null
        }
    }

    fun adapt(): List<Stage> {
        return if (executed.setUsedFuel(requiredFuel).matchesRestrictions()) {
            listOf(executed)
        } else {
            val filled = calculateFuelTanks()
            if (filled.matchesRestrictions()) {
                listOf(filled)
            } else {
                stage.engine.morph()
                        .map { stage.setEngine(it) }
                        .map { ManeuverExecutor(tanksReg, it, maneuver) }
                        .flatMap { it.adapt() }
            }
        }
    }
}

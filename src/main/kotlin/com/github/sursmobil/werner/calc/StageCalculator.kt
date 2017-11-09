package com.github.sursmobil.werner.calc

import com.github.sursmobil.werner.model.*

/**
 * Created by sj on 08/11/2017.
 */

val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()

class StageCalculator(val stage: Stage) {
    private fun burnTime(fuelVol: Double) = fuelVol / stage.engine.fuelVolUsage
    private val fuelMassUsage = stage.engine.fuelMassUsage
    private fun thrustForce(env: Env) = env.thrust(stage.engine.thrust)

    private fun thrust(maneuver: Maneuver) =
            maneuver.env.thrust(stage.engine.thrust)

    fun deltaVForFuelVol(env: Env, fuelVol: Double): Double {
        val burnTime = burnTime(fuelVol)
        val fuelMass = fuelVol * stage.engine.fuelType.density
        val totalMass = stage.rawMass + fuelMass
        val thrustForce = thrustForce(env)

        // dV = integrate thrust / (totalMass - t * fuelMassUsage) for t=0 to burnTime
        return -thrustForce * Math.log(1 - burnTime * fuelMassUsage / totalMass) / fuelMassUsage
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

    private fun fillTanks(fuelToFill: Double, tolerance: Double): Tanks {
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

    private fun addSmallestTank(tanks: Tanks) =
            tanks.add(stage.engine.tankFamily.smallest!!, 1)

    fun calculateFuelTanks(maneuver: Maneuver, tolerance: Double = 0.1): StageCalculator {
        val requiredFuel = requiredFuel(maneuver)
        if (stage.tanks.vol >= requiredFuel)
            return this

        val tanks = fillTanks(requiredFuel, tolerance)
        return StageCalculator(stage.setTanks(tanks))
                .calculateFuelTanks(maneuver, tolerance)
    }
}
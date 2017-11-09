package com.github.sursmobil.werner.calc

import com.github.sursmobil.werner.model.Env
import com.github.sursmobil.werner.model.Maneuver
import com.github.sursmobil.werner.model.Stage

/**
 * Created by sj on 08/11/2017.
 */
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

        return -thrustForce * Math.log(1 - burnTime * fuelMassUsage / totalMass) / fuelMassUsage
    }

    fun requiredFuel(maneuver: Maneuver): Double {
        val c1 = - maneuver.dV * stage.engine.fuelMassUsage / thrust(maneuver)
        val c2 = 1 - Math.exp(c1)
        val c3 = stage.rawMass * c2
        val c4 = stage.engine.fuelMassUsage / stage.engine.fuelVolUsage
        val c5 = c2 * stage.engine.fuelType.density
        return c3 / (c4 - c5)
    }
}
package com.github.sursmobil.werner.calc

import com.github.sursmobil.werner.model.Env
import com.github.sursmobil.werner.model.Stage

/**
 * Created by sj on 08/11/2017.
 */
class StageCalculator(val stage: Stage) {
    private fun burnTime(fuelVol: Double) = fuelVol / stage.engine.fuelVolUsage
    private fun fuelMass(fuelVol: Double) = stage.engine.fuelType.fuelMass(fuelVol)
    private val fuelMassUsage = stage.engine.fuelMassUsage
    private fun thrustForce(env: Env) = env.thrust(stage.engine.thrust)

    private fun deltaVForFuelVol(env: Env, fuelVol: Double): Double {
        val burnTime = burnTime(fuelVol)
        val fuelMass = fuelMass(fuelVol)
        val totalMass = stage.rawMass + fuelMass
        val thrustForce = thrustForce(env)

        return -thrustForce * Math.log(1 - burnTime * fuelMassUsage / totalMass) / fuelMassUsage
    }
}
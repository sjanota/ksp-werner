package com.github.sursmobil.werner

import com.github.sursmobil.werner.calc.Restrictions
import com.github.sursmobil.werner.calc.StageCalculator
import com.github.sursmobil.werner.db.load
import com.github.sursmobil.werner.model.*

/**
 * Created by sj on 08/11/2017.
 */


fun main(args: Array<String>) {
    val engines = load()
    val engine = engines.byName("Poodle")
    val stage = Stage(engine, Payload(13.23, 0))
    val maneuver = Maneuver(Planet.KERBIN, 1200.0, Env.VAC, listOf(
            Restrictions.minTWR(1.0),
            Restrictions.maxBurnTime(90)
    ))
    val calc = StageCalculator(stage)
            .calculateFuelTanks(maneuver)
    println(calc.stage.tanks.vol)
    println(calc.canExecute(maneuver))
    println(calc.currentTWR(maneuver))
    println(calc.burnTime())
}
package com.github.sursmobil.werner

import com.github.sursmobil.werner.calc.StageCalculator
import com.github.sursmobil.werner.db.load
import com.github.sursmobil.werner.model.*

/**
 * Created by sj on 08/11/2017.
 */


fun main(args: Array<String>) {
    val engines = load()
    val engine = engines.byName("Poodle")
    val stage = Stage(engine, Payload(6.98, 0))
    val calc = StageCalculator(stage)
    println(calc.requiredFuel(Maneuver(Planet(), 1200.0, Env.VAC)))
}
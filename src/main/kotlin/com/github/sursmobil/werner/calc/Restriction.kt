package com.github.sursmobil.werner.calc

import com.github.sursmobil.werner.model.Maneuver

/**
 * Created by sj on 14/11/2017.
 */
typealias StageRestriction = (StageCalculator) -> Boolean
typealias ManeuverRestriction = (Maneuver) -> StageRestriction

object Restrictions {
    fun maxBurnTime(seconds: Long): ManeuverRestriction = { m -> { stage ->
        stage.burnTimeToDeltaV(m) <= seconds
    } }

    fun minTWR(minTWR: Double): ManeuverRestriction = { m -> { stage ->
        stage.currentTWR(m) >= minTWR
    } }
}
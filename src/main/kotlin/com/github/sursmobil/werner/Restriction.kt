package com.github.sursmobil.werner

import com.github.sursmobil.werner.model.Maneuver

typealias StageRestriction = (StageCalculator) -> Boolean
typealias ManeuverRestriction = (Maneuver) -> StageRestriction

object Restrictions {
    fun maxBurnTime(seconds: Long): ManeuverRestriction = { m -> { stage ->
        stage.burnTimeToDeltaV(m) <= seconds
    } }

    fun minTWR(minTWR: Double): ManeuverRestriction = { m -> { stage ->
        m.twr(stage.stage) >= minTWR
    } }
}
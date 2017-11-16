package com.github.sursmobil.werner

import com.github.sursmobil.werner.model.Maneuver
import com.github.sursmobil.werner.model.Stage

typealias StageRestriction = (Stage) -> Boolean
typealias ManeuverRestriction = (Maneuver) -> StageRestriction

object Restrictions {
    fun maxBurnTime(seconds: Long): ManeuverRestriction = { m -> { stage ->
        StageCalculator(stage).burnTimeToDeltaV(m) <= seconds
    } }

    fun minTWR(minTWR: Double): ManeuverRestriction = { m -> { stage ->
        m.twr(stage) >= minTWR
    } }
}
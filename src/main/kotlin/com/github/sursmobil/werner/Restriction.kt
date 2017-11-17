package com.github.sursmobil.werner

import com.github.sursmobil.werner.data.tanks.TanksRegistry
import com.github.sursmobil.werner.model.Maneuver
import com.github.sursmobil.werner.model.Stage

typealias StageRestriction = (Stage) -> Boolean
typealias ManeuverRestriction = (Maneuver) -> StageRestriction
typealias ManeuverRestrictionFactory = (TanksRegistry) -> ManeuverRestriction

object Restrictions {
    fun maxBurnTime(seconds: Long): ManeuverRestrictionFactory = { tanksReg -> { m -> { stage ->
        StageCalculator(tanksReg, stage).burnTimeToDeltaV(m) <= seconds
    } }}

    fun minTWR(minTWR: Double): ManeuverRestrictionFactory ={ _ ->  { m -> { stage ->
        m.twr(stage) >= minTWR
    } } }
}
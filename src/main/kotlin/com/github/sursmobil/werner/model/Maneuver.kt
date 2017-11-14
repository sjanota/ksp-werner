package com.github.sursmobil.werner.model

import com.github.sursmobil.werner.calc.ManeuverRestriction

/**
 * Created by sj on 08/11/2017.
 */
class Maneuver(
        val planet: Planet,
        val dV: Double,
        val env: Env,
        genericRestrictions: Collection<ManeuverRestriction> = emptyList()
) {
    val restrictions = genericRestrictions.map { it(this) }
}

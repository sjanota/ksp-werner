package com.github.sursmobil.werner.model

import com.github.sursmobil.werner.calc.ManeuverRestriction


class Maneuver(
        val planet: Planet,
        val dV: Double,
        val env: Env,
        genericRestrictions: Collection<ManeuverRestriction> = emptyList()
) {
    val restrictions = genericRestrictions.map { it(this) }
}

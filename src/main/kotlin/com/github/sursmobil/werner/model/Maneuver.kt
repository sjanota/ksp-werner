package com.github.sursmobil.werner.model

import com.github.sursmobil.werner.ManeuverRestriction


class Maneuver(
        val planet: Planet,
        val dV: Double,
        val env: Env,

        genericRestrictions: Collection<ManeuverRestriction> = emptyList()
) {
    val restrictions = genericRestrictions.map { it(this) }

    fun twr(mass: MassWithThrust) = env.thrust(mass) / planet.weight(mass)

    override fun toString(): String = "Maneuver(planet=$planet, dV=$dV, env=$env)"
}

package com.github.sursmobil.werner

import com.github.sursmobil.werner.model.*
import com.github.sursmobil.werner.model.Env.VAC
import com.github.sursmobil.werner.model.Planet.KERBIN

class RocketBuilder private constructor() {
    private var rockets: Collection<Rocket> = listOf(Rocket())

    companion object {
        fun rocket(f: RocketBuilder.() -> Unit): Rocket? {
            val builder = RocketBuilder()
            builder.f()
            return builder.rockets.minBy { it.cost }
        }
    }

    fun stage(f: StageBuilder.() -> Unit) {
        val builder = StageBuilder()
        builder.f()
        rockets = rockets
                .flatMap { it.nextStages(builder.payload) {
                    addManeuvers(it, builder.maneuvers)
                } }
    }

    private fun addManeuvers(stage: StageCalculator, maneuvers: List<Maneuver>): List<StageCalculator> {
        return if (maneuvers.isEmpty()) {
            listOf(stage)
        } else {
            val added = stage.addManeuver(maneuvers.head)
            added.flatMap { addManeuvers(it, maneuvers.tail) }
        }
    }

    class StageBuilder internal constructor() {
        var payload: Payload = Payload.create(0.0, 0)
        internal val maneuvers = mutableListOf<Maneuver>()

        fun maneuver(f: ManeuverBuilder.() -> Unit) {
            val builder = ManeuverBuilder()
            builder.f()
            val maneuver = builder.build()
            maneuvers.add(maneuver)
        }

        class ManeuverBuilder internal constructor() {
            private var planet: Planet = KERBIN
            var deltaV: Double = -1.0
            var env: Env = VAC
            private val restrictions = mutableListOf<ManeuverRestriction>()

            fun restriction(restriction: ManeuverRestriction) {
                restrictions.add(restriction)
            }

            internal fun build() = if(deltaV > 0)
                Maneuver(planet, deltaV, env, restrictions.toList())
            else
                throw Exception("Delta V must be specified for maneuver")
        }
    }
}


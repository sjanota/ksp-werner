package com.github.sursmobil.werner

import com.github.sursmobil.werner.data.DB
import com.github.sursmobil.werner.model.*
import com.github.sursmobil.werner.model.Env.VAC
import com.github.sursmobil.werner.model.Planet.KERBIN

class RocketBuilder private constructor(
        private val db: DB
) {
    private var rockets: Collection<Rocket> = listOf(Rocket())

    companion object {
        fun rocket(db: DB = DB.loadFromResources(), f: RocketBuilder.() -> Unit): Rocket? {
            val builder = RocketBuilder(db)
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

    private fun addManeuvers(stage: Stage, maneuvers: List<Maneuver>): List<Stage> {
        return if (maneuvers.isEmpty()) {
            listOf(stage)
        } else {
            val added = StageCalculator(db, stage).addManeuver(maneuvers.head)
            added.flatMap { addManeuvers(it, maneuvers.tail) }
        }
    }

   inner class StageBuilder internal constructor() {
        var payload: Payload = Payload.create(mass = 0.0, cost = 0)
        internal val maneuvers = mutableListOf<Maneuver>()

        fun maneuver(f: ManeuverBuilder.() -> Unit) {
            val builder = ManeuverBuilder()
            builder.f()
            val maneuver = builder.build()
            maneuvers.add(maneuver)
        }

        inner class ManeuverBuilder internal constructor() {
            private var planet: Planet = KERBIN
            private val restrictions = mutableListOf<ManeuverRestriction>()

            var deltaV: Double = -1.0
            var env: Env = VAC

            fun restriction(restriction: ManeuverRestrictionFactory) {
                restrictions.add(restriction(this@RocketBuilder.db))
            }

            internal fun build(): Maneuver {
                return if(deltaV > 0) {
                    Maneuver(planet, deltaV, env, restrictions.toList())
                } else {
                    throw Exception("Delta V must be specified for maneuver")
                }
            }
        }
    }
}


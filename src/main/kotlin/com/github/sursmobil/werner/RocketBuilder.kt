package com.github.sursmobil.werner

import com.github.sursmobil.werner.calc.ManeuverRestriction
import com.github.sursmobil.werner.calc.StageCalculator
import com.github.sursmobil.werner.model.*
import com.github.sursmobil.werner.model.Env.VAC
import com.github.sursmobil.werner.model.Planet.KERBIN

class RocketBuilder private constructor() {
    private var stages: Collection<Stage> = listOf(Stage(Engine.None, Payload.create(0.0,0)))

    companion object {
        fun rocket(f: RocketBuilder.() -> Unit): Stage? {
            val builder = RocketBuilder()
            builder.f()
            return builder.stages.minBy { it.cost }
        }
    }

    fun stage(f: StageBuilder.() -> Unit) {
        val builder = StageBuilder()
        builder.f()
        stages = stages
                .map { Stage(Engine.None, it + Payload.create(builder.payload, 0)) }
                .flatMap { StageCalculator(it).addManeuver(builder.maneuvers[0]) }
                .map { it.stage }
        println(stages.map { it.cost })
    }

    class StageBuilder internal constructor() {
        var payload: Double = 0.0
        internal val maneuvers = mutableListOf<Maneuver>()

        fun maneuver(f: ManeuverBuilder.() -> Unit) {
            val builder = ManeuverBuilder()
            builder.f()
            val maneuver = builder.build()
            maneuvers.add(maneuver)
        }

        class ManeuverBuilder internal constructor() {
            var planet: Planet = KERBIN
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


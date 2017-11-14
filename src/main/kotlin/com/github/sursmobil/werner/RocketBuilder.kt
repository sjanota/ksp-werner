package com.github.sursmobil.werner

import com.github.sursmobil.werner.RocketBuilder.Companion.rocket
import com.github.sursmobil.werner.calc.ManeuverRestriction
import com.github.sursmobil.werner.calc.Restrictions.maxBurnTime
import com.github.sursmobil.werner.calc.Restrictions.minTWR
import com.github.sursmobil.werner.calc.StageCalculator
import com.github.sursmobil.werner.db.load
import com.github.sursmobil.werner.model.*
import com.github.sursmobil.werner.model.Env.ASL
import com.github.sursmobil.werner.model.Env.VAC
import com.github.sursmobil.werner.model.Planet.KERBIN
import com.github.sursmobil.werner.model.Planet.MINMUS

/**
 * Created by sj on 14/11/2017.
 */
class RocketBuilder private constructor() {
    companion object {
        private val engines = load()

        fun rocket(f: RocketBuilder.() -> Unit) {
            val builder = RocketBuilder()
            builder.f()
        }
    }

    fun stage(f: StageBuilder.() -> Unit) {
        val builder = StageBuilder()
        builder.f()
        val initStage = Stage(engines.byName("Poodle"), Payload(builder.payload, 0))
        val initCalc = StageCalculator(initStage)
        val calc = builder.maneuvers.fold(initCalc) { acc, m -> acc.calculateFuelTanks(m) }
        println(calc.stage.tanks.vol)
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

            internal fun build() = Maneuver(planet, deltaV, env, restrictions.toList())
        }
    }
}

fun example() {
    rocket {
        stage {
            payload = 6.23

            maneuver {
                planet = MINMUS
                deltaV = 250.0

                restriction(maxBurnTime(90))
            }

            maneuver {
                planet = MINMUS
                deltaV = 200.0

                restriction(minTWR(1.5))
            }
        }
        stage {
            maneuver {
                deltaV = 1200.0

                restriction(maxBurnTime(120))
            }
        }
        stage {
            maneuver {
                deltaV = 2500.0
                env = ASL

                restriction(minTWR(1.5))
            }
        }
    }
}

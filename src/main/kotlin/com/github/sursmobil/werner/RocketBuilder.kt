package com.github.sursmobil.werner

import com.github.sursmobil.werner.RocketBuilder.Companion.rocket
import com.github.sursmobil.werner.calc.ManeuverRestriction
import com.github.sursmobil.werner.calc.Restrictions.maxBurnTime
import com.github.sursmobil.werner.calc.Restrictions.minTWR
import com.github.sursmobil.werner.model.Env
import com.github.sursmobil.werner.model.Env.ASL
import com.github.sursmobil.werner.model.Env.VAC
import com.github.sursmobil.werner.model.Planet
import com.github.sursmobil.werner.model.Planet.KERBIN
import com.github.sursmobil.werner.model.Planet.MINMUS

/**
 * Created by sj on 14/11/2017.
 */
class RocketBuilder private constructor() {
    companion object {
        fun rocket(f: RocketBuilder.() -> Unit) {

        }
    }

    fun stage(f: StageBuilder.() -> Unit) {

    }

    class StageBuilder private constructor() {
        var payload: Double = 0.0

        fun maneuver(f: ManeuverBuilder.() -> Unit) {

        }

        class ManeuverBuilder private constructor() {
            var planet: Planet = KERBIN
            var deltaV: Double = -1.0
            var env: Env = VAC

            fun restriction(restriction: ManeuverRestriction) {

            }
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

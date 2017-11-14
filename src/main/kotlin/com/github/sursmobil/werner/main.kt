package com.github.sursmobil.werner

import com.github.sursmobil.werner.RocketBuilder.Companion.rocket
import com.github.sursmobil.werner.calc.Restrictions.maxBurnTime
import com.github.sursmobil.werner.calc.Restrictions.minTWR


fun main(args: Array<String>) {
    rocket {
        stage {
            payload = 13.23

            maneuver {
                deltaV = 1200.0

                restriction(minTWR(1.12))
                restriction(maxBurnTime(92))
            }
        }
    }
}
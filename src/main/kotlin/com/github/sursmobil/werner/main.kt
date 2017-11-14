package com.github.sursmobil.werner

import com.github.sursmobil.werner.RocketBuilder.Companion.rocket
import com.github.sursmobil.werner.calc.Restrictions.maxBurnTime
import com.github.sursmobil.werner.calc.Restrictions.minTWR
import com.github.sursmobil.werner.db.load


fun main(args: Array<String>) {
    load()
    rocket {
        stage {
            payload = 13.23

            maneuver {
                deltaV = 1200.0

                restriction(minTWR(1.0))
                restriction(maxBurnTime(120))
            }
        }
    }
}
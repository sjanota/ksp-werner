package com.github.sursmobil.werner

import com.github.sursmobil.werner.RocketBuilder.Companion.rocket
import com.github.sursmobil.werner.calc.Restrictions
import com.github.sursmobil.werner.calc.Restrictions.maxBurnTime
import com.github.sursmobil.werner.calc.Restrictions.minTWR
import com.github.sursmobil.werner.calc.StageCalculator
import com.github.sursmobil.werner.db.load
import com.github.sursmobil.werner.model.*

/**
 * Created by sj on 08/11/2017.
 */


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
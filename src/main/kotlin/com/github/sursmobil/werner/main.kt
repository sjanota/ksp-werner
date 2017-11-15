package com.github.sursmobil.werner

import com.github.sursmobil.werner.RocketBuilder.Companion.rocket
import com.github.sursmobil.werner.Restrictions.maxBurnTime
import com.github.sursmobil.werner.Restrictions.minTWR
import com.github.sursmobil.werner.db.load
import com.github.sursmobil.werner.model.Env.ASL
import com.github.sursmobil.werner.model.Env.VAC
import com.github.sursmobil.werner.model.Payload


fun main(args: Array<String>) {
    load()
    val result = rocket {
        stage {
            payload = Payload.create(6.98, 17350)

            maneuver {
                deltaV = 1200.0

                restriction(maxBurnTime(100))
            }

        }

        stage {
            payload = Payload.create(0.4, 550)

            maneuver {
                deltaV = 2500.0
                env = ASL

                restriction(minTWR(1.5))
            }
        }
    }

    for (stage in result!!.stages) {
        println(stage)
    }
}
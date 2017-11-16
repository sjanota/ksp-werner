package com.github.sursmobil.werner

import com.github.sursmobil.werner.Restrictions.maxBurnTime
import com.github.sursmobil.werner.Restrictions.minTWR
import com.github.sursmobil.werner.RocketBuilder.Companion.rocket
import com.github.sursmobil.werner.model.Env
import com.github.sursmobil.werner.model.Payload
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

object Werner: Spek({
    describe("a rocket builder") {
        on("minmus adventure") {
            val result = rocket {
                stage {
                    payload = Payload.create(mass = 7.844 - 0.88, cost = 17350)

                    maneuver {
                        deltaV = 1200.0

                        restriction(maxBurnTime(seconds = 100))
                    }

                }

                stage {
                    payload = Payload.create(mass = 0.4 + 0.88, cost = 550 + 4 * 640)

                    maneuver {
                        deltaV = 3500.0
                        env = Env.ASL

                        restriction(minTWR(minTWR = 1.5))
                    }
                }
            }

            it("should have result") {
                assertNotNull(result)
            }


            it("should have last stage worth 48310"){
                val stage = result!!.stages.last()
                assertEquals(48310, stage.cost)
            }

            it("should have middle stage worth 19450") {
                val stage = result!!.stages[result.stages.size-2]
                assertEquals(19450, stage.cost)
            }
        }
    }
})
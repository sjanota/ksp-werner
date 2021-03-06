package com.github.sursmobil.werner.model

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.sursmobil.werner.data.tanks.BaseTanksRegistry
import com.github.sursmobil.werner.data.tanks.TanksRegistry
import com.github.sursmobil.werner.head
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by sj on 16/11/2017.
 */

val engineRockomax = BaseEngine("2.5", 0, 0.0, 0.0, 0.0, FuelType.LiquidFuel, Thrust.None, 2.5)
val engineKebordyne = BaseEngine("3.75", 0, 0.0, 0.0, 0.0, FuelType.LiquidFuel, Thrust.None, 3.75)

object TankFamilyTests : Spek({
    given("YAML parser") {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

        on("parsing YAML with scaled tanks") {
            val yaml = """
            - type: scaled
              name: Rockomax
              fuelType: liquid
              factors: {1: 800, 2: 1550, 4: 3000, 8: 5750}
              nominalVol: 360
              mount:
                type: main
                size: 2.5
            """.trimIndent()
            val tanks = mapper.readValue<TanksRegistry>(yaml, object : TypeReference<TanksRegistry>() {})

            it("there are as many tanks as factors") {
                assertEquals(4, tanks.size)
            }
            it("tank costs should be included") {
                assertEquals(listOf(800, 1550, 3000, 5750), tanks.map { it.cost })
            }
            it("tank volumes are multiplications of nominal volume") {
                assertEquals(listOf(360.0, 720.0, 1440.0, 2880.0), tanks.map { it.vol })
            }
            it("every tank has liquid fuel") {
                assertTrue(tanks.all { it.fuelType == FuelType.LiquidFuel })
            }
            it("every tank can be mounted to 2.5 engine") {
                assertEquals(4, tanks.onlyMountable(engineRockomax).size)
            }
            it("none of the tanks can be mounted to 3.75 engine") {
                assertEquals(0, tanks.onlyMountable(engineKebordyne).size)
            }
        }

        on("parsing YAML with static tank") {
            val yaml = """
            - type: static
              name: Oscar
              fuelType: liquid
              vol: 18
              cost: 70
              mount:
                type: radial
            """.trimIndent()
            val tanks = mapper.readValue<TanksRegistry>(yaml, object : TypeReference<TanksRegistry>() {})

            it("there is one tank") {
                assertEquals(1, tanks.size)
            }
            it("cost of the tank is 70") {
                assertEquals(70, tanks.first().cost)
            }
            it("volume of the tank is 18") {
                assertEquals(18.0, tanks.first().vol)
            }
            it("tank uses liquid fuel") {
                assertEquals(FuelType.LiquidFuel, tanks.first().fuelType)
            }
            it("tank name is 'Oscar") {
                assertEquals("Oscar", tanks.first().name)
            }
            it("every tank can be mounted to 2.5 engine") {
                assertEquals(1, tanks.onlyMountable(engineRockomax).size)
            }
            it("every tank can be mounted to 3.75 engine") {
                assertEquals(1, tanks.onlyMountable(engineKebordyne).size)
            }
        }
    }
})
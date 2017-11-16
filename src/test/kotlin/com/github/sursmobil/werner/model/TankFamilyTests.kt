package com.github.sursmobil.werner.model

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Created by sj on 16/11/2017.
 */

object TankFamilyTests : Spek({
    describe("Parse YAML") {
        val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

        on("scaled tanks") {
            val yaml = """
            tanks:
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
                assertEquals(4, tanks.tanks.size)
            }
            it("tank costs should be included") {
                assertEquals(listOf(800, 1550, 3000, 5750), tanks.tanks.map { it.cost })
            }
            it("tank volumes are multiplications of nominal volume") {
                assertEquals(listOf(360.0, 720.0, 1440.0, 2880.0), tanks.tanks.map { it.vol })
            }
            it("every tank has liquid fuel") {
                assertTrue(tanks.tanks.all { it.fuelType == FuelType.LiquidFuel })
            }
            it("every tank can be mounted to 2.5 engine") {
                val engine = BaseEngine("2.5", 0, 0.0, 0.0, 0.0, TankFamily.None, FuelType.LiquidFuel, Thrust.None, 2.5)
                assertTrue(tanks.tanks.all { it.mount.canBeAttached(engine) })
            }
        }
    }
})
package com.github.sursmobil.werner.model

import com.github.sursmobil.werner.StageCalculator

class Rocket(private val stack: List<StageCalculator> = listOf(StageCalculator(Stage.None))) {
    fun nextStages(payload: Payload, f: (StageCalculator) -> List<StageCalculator>): List<Rocket> {
        val nextStage = Stage(Engine.None, stack.last().stage + payload)
        val next = f(StageCalculator(nextStage, stack.last().restrictions))
        return next.map { Rocket(stack + listOf(it)) }
    }

    val cost: Int = stack.last().stage.cost
    val stages = stack.map { it.stage }
}
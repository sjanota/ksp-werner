package com.github.sursmobil.werner.model

import com.github.sursmobil.werner.StageCalculator

class Rocket(
        private val stack: List<StageCalculator> = listOf(StageCalculator(Stage.Empty))
) {
    val cost: Int = stack.last().stage.cost
    val stages = stack.map { it.stage }

    fun nextStages(payload: Payload, f: (StageCalculator) -> List<StageCalculator>): List<Rocket> {
        val newPayload = stack.last().stage + payload
        val nextStage = Stage(engine = Engine.None, payload = newPayload)
        val nextStageCalc = StageCalculator(nextStage, stack.last().restrictions)
        val nextStages = f(nextStageCalc)
        return nextStages.map { Rocket(stack + listOf(it)) }
    }
}
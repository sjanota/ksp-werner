package com.github.sursmobil.werner.model

class Rocket(
        val stages: List<Stage> = listOf(Stage.Empty)
) {
    val cost: Int = stages.last().cost

    fun nextStages(payload: Payload, f: (Stage) -> List<Stage>): List<Rocket> {
        val baseNextStage = stages.last().nextStage(payload)
        val nextStages = f(baseNextStage)
        return nextStages.map { Rocket(stages + listOf(it)) }
    }
}
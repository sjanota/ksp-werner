package com.github.sursmobil.werner.model

interface Payload {
    val mass: Double
    val cost: Int

    operator fun plus(other: Payload) = create(mass + other.mass, cost + other.cost)

    companion object {
        fun create(mass: Double, cost: Int): Payload = object : Payload {
            override val mass: Double = mass
            override val cost: Int = cost
            override fun toString() = "Payload(mass=$mass, cost=$cost)"
        }

        val Empty = create(0.0, 0)
    }
}
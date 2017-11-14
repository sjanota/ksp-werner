package com.github.sursmobil.werner.model

interface Payload {
    val mass: Double
    val cost: Int

    companion object {
        fun create(mass: Double, cost: Int): Payload = object : Payload {
            override val mass: Double
                get() = mass
            override val cost: Int
                get() = cost
        }
    }

    operator fun plus(other: Payload) = create(mass + other.mass, cost + other.cost)
}
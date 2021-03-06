package com.github.sursmobil.werner.model


enum class Env {
    ASL {
        override fun thrust(thrust: WithThrust): Double = thrust.thrust.asl
    },
    VAC {
        override fun thrust(thrust: WithThrust): Double = thrust.thrust.vac
    };

    abstract fun thrust(thrust: WithThrust): Double
}

package com.github.sursmobil.werner.model


enum class Env {
    ASL {
        override fun thrust(thrust: Thrust): Double = thrust.asl
    },
    VAC {
        override fun thrust(thrust: Thrust): Double = thrust.vac
    };

    abstract fun thrust(thrust: Thrust): Double
}

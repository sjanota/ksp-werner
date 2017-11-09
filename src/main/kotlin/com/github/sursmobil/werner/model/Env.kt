package com.github.sursmobil.werner.model

/**
 * Created by sj on 08/11/2017.
 */

enum class Env {
    ASL {
        override fun thrust(thrust: Thrust): Double = thrust.asl
    },
    VAC {
        override fun thrust(thrust: Thrust): Double = thrust.vac
    };

    abstract fun thrust(thrust: Thrust): Double
}

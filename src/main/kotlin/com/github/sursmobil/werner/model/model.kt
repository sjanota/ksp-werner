package com.github.sursmobil.werner.model

/**
 * Created by sj on 16/11/2017.
 */

interface Mass {
    val mass: Double
}

interface WithThrust {
    val thrust: Thrust
}

interface MountSurface {
    val size: Double
}

interface Mountable {
    fun canBeMounted(surface: MountSurface): Boolean
}

interface MassWithThrust: Mass, WithThrust
package com.github.sursmobil.werner.model

private const val G: Double = 9.81

enum class Planet(val gravity: Double) {
    KERBIN(G), MINMUS(0.005 * G);

    fun weight(mass: Mass) = mass.mass * gravity
}
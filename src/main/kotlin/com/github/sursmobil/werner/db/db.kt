package com.github.sursmobil.werner.db

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.sursmobil.werner.model.Engine
import com.github.sursmobil.werner.model.TankFamily

fun load() {
    loadTankFamilies()
    loadEngines()
}

class Anchor

private val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()

private fun loadTankFamilies() {
    val resource = Anchor::class.java.getResourceAsStream("tank-families.yml")
    mapper.readValue<List<TankFamily>>(resource, object : TypeReference<List<TankFamily>>() {})
}

private fun loadEngines() {
    val resource = Anchor::class.java.getResourceAsStream("engines.yml")
    mapper.readValue<List<Engine>>(resource, object : TypeReference<List<Engine>>() {})
}
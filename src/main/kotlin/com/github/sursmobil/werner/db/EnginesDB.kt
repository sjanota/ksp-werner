package com.github.sursmobil.werner.db

import com.github.sursmobil.werner.model.Engine

data class EnginesDB(private val engines: List<Engine>) {
    private val indexByName = engines.associateBy { it.name }

    fun byName(name: String) = indexByName[name] ?: throw Exception("Engine '$name' not found")
}
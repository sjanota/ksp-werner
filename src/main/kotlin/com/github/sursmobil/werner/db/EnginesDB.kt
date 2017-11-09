package com.github.sursmobil.werner.db

import com.github.sursmobil.werner.model.Engine

/**
 * Created by sj on 08/11/2017.
 */
data class EnginesDB(val engines: List<Engine>) {
    private val indexByName = engines.associateBy { it.name }

    fun byName(name: String) = indexByName[name] ?: throw Exception("Engine '$name' not found")
}
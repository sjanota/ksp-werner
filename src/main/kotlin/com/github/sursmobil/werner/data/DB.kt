package com.github.sursmobil.werner.data

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.github.sursmobil.werner.data.engines.EnginesRegistry
import com.github.sursmobil.werner.data.tanks.TanksRegistry
import java.io.InputStream

class DB (
        private val tanksReg: TanksRegistry,
        private val enginesReg: EnginesRegistry
) : TanksRegistry by tanksReg, EnginesRegistry by enginesReg {
    companion object {
        fun loadFromResources(): DB = loadFromStreams(
                DB::class.java.getResourceAsStream("engines.yml"),
                DB::class.java.getResourceAsStream("tanks.yml")
        )

        fun loadFromStreams(enginesStream: InputStream, tanksStream: InputStream): DB {
            val mapper = ObjectMapper(YAMLFactory()).registerKotlinModule()
            val tanksRegistry = mapper.readValue(tanksStream, TanksRegistry::class.java)
            val engines = mapper.readValue<EnginesRegistry>(enginesStream, object : TypeReference<EnginesRegistry>() {})
            return DB(tanksRegistry, engines)
        }
    }
}


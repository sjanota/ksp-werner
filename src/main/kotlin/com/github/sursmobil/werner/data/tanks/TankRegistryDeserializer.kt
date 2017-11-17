package com.github.sursmobil.werner.data.tanks

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

internal class TankRegistryDeserializer : JsonDeserializer<BaseTanksRegistry>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): BaseTanksRegistry {
        val tanks = p!!.readValueAs<List<TankFactory>>(object : TypeReference<List<TankFactory>>() {})
                .asSequence().toList()
                .flatMap { it.createTanks() }
                .toSet()
        return BaseTanksRegistry(tanks)
    }
}
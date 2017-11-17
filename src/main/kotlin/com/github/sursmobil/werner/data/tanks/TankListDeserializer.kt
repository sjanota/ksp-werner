package com.github.sursmobil.werner.data.tanks

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

internal class TankListDeserializer : JsonDeserializer<List<Tank>>() {
    override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): List<Tank> {
        return p!!.readValueAs<List<TankFactory>>(object : TypeReference<List<TankFactory>>() {})
                .asSequence().toList()
                .flatMap { it.createTanks() }
    }
}
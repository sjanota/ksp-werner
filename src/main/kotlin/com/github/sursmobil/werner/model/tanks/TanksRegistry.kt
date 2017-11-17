package com.github.sursmobil.werner.model.tanks

import com.fasterxml.jackson.databind.annotation.JsonDeserialize

class TanksRegistry(
        @JsonDeserialize(using = TankListDeserializer::class)
        val tanks: List<Tank>
) {

}


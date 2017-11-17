package com.github.sursmobil.werner.model.tanks

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.sursmobil.werner.model.MountSurface

class TanksRegistry(
        @JsonDeserialize(using = TankListDeserializer::class)
        val tanks: List<Tank>
) {
    fun getMountable(surface: MountSurface): List<Tank> = tanks
            .filter { it.canBeMounted(surface) }
}


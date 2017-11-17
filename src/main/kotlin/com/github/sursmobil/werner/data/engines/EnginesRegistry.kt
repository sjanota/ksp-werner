package com.github.sursmobil.werner.data.engines

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.sursmobil.werner.model.Engine

/**
 * Created by sj on 17/11/2017.
 */
@JsonDeserialize(`as` = BaseEnginesRegistry::class)
interface EnginesRegistry {
    val engines: List<Engine>
}

class BaseEnginesRegistry(
        override val engines: List<Engine>
) : EnginesRegistry
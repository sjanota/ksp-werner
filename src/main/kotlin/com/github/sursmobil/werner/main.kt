package com.github.sursmobil.werner

import com.github.sursmobil.werner.db.load
import com.github.sursmobil.werner.model.Payload
import com.github.sursmobil.werner.model.Stage

/**
 * Created by sj on 08/11/2017.
 */


fun main(args: Array<String>) {
    val engines = load()
    val engine = engines.byName("Poodle")
    val stage = Stage(engine, Payload(6.98, 0))
}
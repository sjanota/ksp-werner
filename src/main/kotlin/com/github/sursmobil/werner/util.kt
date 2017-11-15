package com.github.sursmobil.werner

/**
 * Created by sj on 15/11/2017.
 */
val <T> List<T>.tail: List<T>
    get() = drop(1)

val <T> List<T>.head: T
    get() = first()
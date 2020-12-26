package de.helfenkannjeder.helfomat.api

val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun randomString(length: Int) = (1..length)
    .map { kotlin.random.Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")
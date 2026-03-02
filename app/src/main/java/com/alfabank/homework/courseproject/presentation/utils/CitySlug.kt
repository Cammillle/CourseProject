package com.alfabank.homework.courseproject.presentation.utils

fun String.getSlug(): String {
    val cityToCodeMap = mapOf(
        "Москва" to "msk",
        "Санкт-Петербург" to "spb",
        "Новосибирск" to "nsk",
        "Екатеринбург" to "ekb",
        "Нижний Новгород" to "nnv",
        "Казань" to "kzn",
        "Выборг" to "vbg",
        "Самара" to "smr",
        "Краснодар" to "krd",
        "Сочи" to "sochi",
        "Уфа" to "ufa",
        "Красноярск" to "krasnoyarsk",
        "Киев" to "kev",
        "Нью-Йорк" to "new-york"
    )
    return cityToCodeMap[this] ?: ""
}

fun String.fromSlug(): String {
    val codeToCityMap = mapOf(
        "msk" to "Москва",
        "spb" to "Санкт-Петербург",
        "nsk" to "Новосибирск",
        "ekb" to "Екатеринбург",
        "nnv" to "Нижний Новгород",
        "kzn" to "Казань",
        "vbg" to "Выборг",
        "smr" to "Самара",
        "krd" to "Краснодар",
        "sochi" to "Сочи",
        "ufa" to "Уфа",
        "krasnoyarsk" to "Красноярск",
        "kev" to "Киев",
        "new-york" to "Нью-Йорк"
    )
    return codeToCityMap[this] ?: ""
}
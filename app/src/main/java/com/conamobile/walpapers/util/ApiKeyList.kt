package com.conamobile.walpapers.util

object ApiKeyList {
    var countGetInt = 0

    fun getApiKeyAutomatic(): String {
        return when (countGetInt) {
            0 -> "Client-ID lOwYkRhXb7OgyGquor9WgJsk1uBNU4zhYjtlWfvMFqo"
            1 -> "Client-ID KR7Tcw-RNnurIM-7JDGj9S-5DUeFhVTx1YNxoR-vRkg"
            2 -> "Client-ID ix3DfiJc2b5ZRdp9ltoB12fgxswMxmij7uvuv0lAGRc"
            3 -> "Client-ID amn3Ew2DTp5WFWMmDiYFyFxewN-6m6yrBFiG0SkZeyg"
            else -> "Client-ID lOwYkRhXb7OgyGquor9WgJsk1uBNU4zhYjtlWfvMFqo"
        }
    }
}
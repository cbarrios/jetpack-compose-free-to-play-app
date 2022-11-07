package com.lalosapps.freetoplay.domain.model

data class MinimumSystemRequirements(
    val graphics: String?,
    val memory: String?,
    val os: String?,
    val processor: String?,
    val storage: String?
) {
    val isNull: Boolean
        get() = graphics == null && memory == null && os == null && processor == null && storage == null
}
package com.cabbagebeyond.model

import java.util.*

data class Role(
    var name: String,
    var features: List<String>,
    var id: String = UUID.randomUUID().toString()
)
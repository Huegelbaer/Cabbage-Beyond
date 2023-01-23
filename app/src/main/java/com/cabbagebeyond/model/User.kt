package com.cabbagebeyond.model


data class User(
    var name: String,
    var email: String,
    var features: List<String>,
    var roles: List<Role>,
    val id: String
)
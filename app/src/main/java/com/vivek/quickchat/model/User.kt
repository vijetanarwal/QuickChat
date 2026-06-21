package com.vijet.quickchat.model

import java.io.Serializable

data class User(
    val name: String,
    var image: String? = null,
    var email: String? = null,
    var token: String? = null,
    val id: String,
    var isOnline: Boolean = false,
    var lastSeen: Long = 0L
) : Serializable
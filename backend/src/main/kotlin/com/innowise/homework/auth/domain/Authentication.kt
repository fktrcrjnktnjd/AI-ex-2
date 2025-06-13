package com.innowise.homework.auth.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "auth")
data class Authentication(
    @Id
    val id: String? = null,
    val name: String,
    val email: String,
    val passwordHash: String
) 
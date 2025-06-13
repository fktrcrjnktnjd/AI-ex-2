package com.innowise.homework.user.domain

import com.innowise.homework.user.domain.Geo

data class Address(
    val street: String,
    val suite: String,
    val city: String,
    val zipcode: String,
    val geo: Geo
) 
package com.innowise.homework.user.application.dto

import com.innowise.homework.user.domain.User
import com.innowise.homework.user.domain.Address
import com.innowise.homework.user.domain.Company

data class UserDto(
    val id: Long,
    val name: String,
    val username: String,
    val email: String,
    val address: Address,
    val phone: String,
    val website: String,
    val company: Company
) {
    companion object {
        fun fromEntity(user: User): UserDto = UserDto(
            id = user.id,
            name = user.name,
            username = user.username,
            email = user.email,
            address = user.address,
            phone = user.phone,
            website = user.website,
            company = user.company
        )
    }
} 
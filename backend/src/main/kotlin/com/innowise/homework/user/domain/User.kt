package com.innowise.homework.user.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import com.innowise.homework.user.application.dto.UserDto

@Document(collection = "users")
data class User(
    @Id
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
        fun fromDto(dto: UserDto): User = User(
            id = dto.id,
            name = dto.name,
            username = dto.username,
            email = dto.email,
            address = dto.address,
            phone = dto.phone,
            website = dto.website,
            company = dto.company
        )
    }
} 
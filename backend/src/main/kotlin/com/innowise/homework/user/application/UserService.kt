package com.innowise.homework.user.application

import com.innowise.homework.user.domain.User
import com.innowise.homework.user.infrastructure.UserRepository
import com.innowise.homework.user.application.dto.UserDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import jakarta.annotation.PostConstruct
import mu.KLogging
import java.util.NoSuchElementException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val restTemplate: RestTemplate,
    @Value("\${jsonplaceholder.users.url}")
    private val jsonPlaceholderUrl: String
) {
    companion object : KLogging()

    @PostConstruct
    fun seedUsersOnStartup() {
        if (userRepository.count() == 0L) {
            val userDtos = restTemplate.getForObject(jsonPlaceholderUrl, Array<UserDto>::class.java)?.toList() ?: emptyList()
            val users = userDtos.map { User.fromDto(it) }
            userRepository.saveAll(users)
            logger.info { "Seeded users from JSONPlaceholder into MongoDB successfully. Count: ${users.size}" }
        }
    }

    fun getUserById(id: Long): UserDto =
        userRepository.findById(id).map { UserDto.fromEntity(it) }.orElseThrow { NoSuchElementException("User not found") }

    fun listUsers(
        id: Long?,
        name: String?,
        username: String?,
        email: String?,
        phone: String?,
        website: String?,
        address: String?,
        company: String?
    ): List<UserDto> {
        return userRepository.findAll().filter { user ->
            (id == null || user.id == id) &&
            (name == null || user.name.contains(name, ignoreCase = true)) &&
            (username == null || user.username.contains(username, ignoreCase = true)) &&
            (email == null || user.email.contains(email, ignoreCase = true)) &&
            (phone == null || user.phone.contains(phone, ignoreCase = true)) &&
            (website == null || user.website.contains(website, ignoreCase = true)) &&
            (address == null || user.address.toString().contains(address, ignoreCase = true)) &&
            (company == null || user.company.toString().contains(company, ignoreCase = true))
        }.map { UserDto.fromEntity(it) }
    }

    fun createUser(userDto: UserDto): UserDto {
        if (userRepository.existsById(userDto.id)) throw IllegalArgumentException("User with id ${userDto.id} already exists")
        val user = User.fromDto(userDto)
        return UserDto.fromEntity(userRepository.save(user))
    }

    fun updateUser(id: Long, userDto: UserDto): UserDto {
        val existing = userRepository.findById(id).orElseThrow { NoSuchElementException("User not found") }
        val updated = userDto.copy(id = id)
        val user = User.fromDto(updated)
        return UserDto.fromEntity(userRepository.save(user))
    }

    fun deleteUser(id: Long) {
        if (!userRepository.existsById(id)) throw NoSuchElementException("User not found")
        userRepository.deleteById(id)
    }
} 
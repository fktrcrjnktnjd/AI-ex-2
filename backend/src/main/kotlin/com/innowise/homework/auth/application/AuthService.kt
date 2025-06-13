package com.innowise.homework.auth.application

import com.innowise.homework.auth.infrastructure.AuthenticationRepository
import com.innowise.homework.auth.domain.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val authenticationRepository: AuthenticationRepository,
    private val jwtUtil: JwtUtil
) {
    private val passwordEncoder = BCryptPasswordEncoder()

    fun register(name: String, email: String, password: String): Authentication {
        if (authenticationRepository.findByEmail(email) != null) {
            throw IllegalArgumentException("Email already registered")
        }
        val passwordHash = passwordEncoder.encode(password)
        val auth = Authentication(name = name, email = email, passwordHash = passwordHash)
        return authenticationRepository.save(auth)
    }

    fun login(email: String, password: String): String {
        val auth = authenticationRepository.findByEmail(email)
            ?: throw IllegalArgumentException("Invalid credentials")
        if (!passwordEncoder.matches(password, auth.passwordHash)) {
            throw IllegalArgumentException("Invalid credentials")
        }
        return jwtUtil.generateToken(auth.email)
    }
} 
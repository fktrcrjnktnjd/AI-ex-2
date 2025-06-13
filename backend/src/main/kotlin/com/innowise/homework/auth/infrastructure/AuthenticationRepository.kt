package com.innowise.homework.auth.infrastructure

import com.innowise.homework.auth.domain.Authentication
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
 
@Repository
interface AuthenticationRepository : MongoRepository<Authentication, String> {
    fun findByEmail(email: String): Authentication?
} 
package com.innowise.homework.user.infrastructure

import com.innowise.homework.user.domain.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
 
@Repository
interface UserRepository : MongoRepository<User, Long> 
package com.innowise.homework.user.resources

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.delete

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    lateinit var jwt: String

    @BeforeEach
    fun setup() {
        val email = "user1@example.com"
        val password = "password123"
        val name = "User One"
        // Register
        mockMvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf(
                "name" to name,
                "email" to email,
                "password" to password
            ))
        }
        // Login
        val loginResult = mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf(
                "email" to email,
                "password" to password
            ))
        }.andReturn()
        jwt = objectMapper.readTree(loginResult.response.contentAsString).get("token").asText()
    }

    @Test
    fun `create, get, update, delete, and list users`() {
        // Create user
        val user = mapOf(
            "id" to 1001,
            "name" to "Test User",
            "username" to "testuser",
            "email" to "testuser2@example.com",
            "address" to mapOf(
                "street" to "Main St",
                "suite" to "Apt 1",
                "city" to "Testville",
                "zipcode" to "12345",
                "geo" to mapOf("lat" to "0", "lng" to "0")
            ),
            "phone" to "123-456-7890",
            "website" to "test.com",
            "company" to mapOf(
                "name" to "TestCo",
                "catchPhrase" to "Testing!",
                "bs" to "test"
            )
        )
        val createResult = mockMvc.post("/users") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $jwt")
            content = objectMapper.writeValueAsString(user)
        }.andExpect {
            status { isOk() }
            jsonPath("$.id") { value(1001) }
        }.andReturn()

        // Get user by id
        mockMvc.get("/users/1001") {
            header("Authorization", "Bearer $jwt")
        }.andExpect {
            status { isOk() }
            jsonPath("$.email") { value("testuser2@example.com") }
        }

        // Update user
        val updatedUser = user.toMutableMap().apply { put("name", "Updated User") }
        mockMvc.put("/users/1001") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $jwt")
            content = objectMapper.writeValueAsString(updatedUser)
        }.andExpect {
            status { isOk() }
            jsonPath("$.name") { value("Updated User") }
        }

        // List users
        mockMvc.get("/users") {
            header("Authorization", "Bearer $jwt")
        }.andExpect {
            status { isOk() }
            jsonPath("$[?(@.id==1001)]") { exists() }
        }

        // Filter users
        mockMvc.get("/users?name=Updated") {
            header("Authorization", "Bearer $jwt")
        }.andExpect {
            status { isOk() }
            jsonPath("$[0].name") { value("Updated User") }
        }

        // Delete user
        mockMvc.delete("/users/1001") {
            header("Authorization", "Bearer $jwt")
        }.andExpect {
            status { isOk() }
        }
    }
} 
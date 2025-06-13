package com.innowise.homework.auth.resources

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {
    @Test
    fun `register and login returns JWT token`() {
        val email = "testuser@example.com"
        val password = "password123"
        val name = "Test User"

        // Register
        mockMvc.post("/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf(
                "name" to name,
                "email" to email,
                "password" to password
            ))
        }.andExpect {
            status { isOk() }
        }

        // Login
        val loginResult = mockMvc.post("/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mapOf(
                "email" to email,
                "password" to password
            ))
        }.andExpect {
            status { isOk() }
            jsonPath("$.token") { exists() }
        }.andReturn()

        val response = loginResult.response.contentAsString
        val token = objectMapper.readTree(response).get("token").asText()
        assertNotNull(token)
    }
} 
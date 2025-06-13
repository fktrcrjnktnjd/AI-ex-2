package com.innowise.homework.auth.resources

import com.innowise.homework.auth.application.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/auth")
class AuthController(private val authService: AuthService) {
    data class LoginRequest(val email: String, val password: String)
    data class RegisterRequest(val name: String, val email: String, val password: String)
    data class AuthResponse(val token: String)

    @PostMapping("/login")
    fun login(@RequestBody req: LoginRequest): ResponseEntity<AuthResponse> =
        ResponseEntity.ok(AuthResponse(authService.login(req.email, req.password)))

    @PostMapping("/register")
    fun register(@RequestBody req: RegisterRequest) =
        ResponseEntity.ok(authService.register(req.name, req.email, req.password))
} 
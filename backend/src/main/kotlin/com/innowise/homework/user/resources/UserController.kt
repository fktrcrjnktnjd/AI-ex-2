package com.innowise.homework.user.resources

import com.innowise.homework.user.application.UserService
import com.innowise.homework.user.application.dto.UserDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(private val userService: UserService) {
    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): UserDto = userService.getUserById(id)

    @GetMapping
    fun listUsers(
        @RequestParam(required = false) id: Long?,
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) username: String?,
        @RequestParam(required = false) email: String?,
        @RequestParam(required = false) phone: String?,
        @RequestParam(required = false) website: String?,
        @RequestParam(required = false) address: String?,
        @RequestParam(required = false) company: String?
    ): List<UserDto> = userService.listUsers(id, name, username, email, phone, website, address, company)

    @PostMapping
    fun createUser(@RequestBody userDto: UserDto): UserDto = userService.createUser(userDto)

    @PutMapping("/{id}")
    fun updateUser(@PathVariable id: Long, @RequestBody userDto: UserDto): UserDto = userService.updateUser(id, userDto)

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long) = userService.deleteUser(id)
} 
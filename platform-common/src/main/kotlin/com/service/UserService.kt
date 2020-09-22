package com.service

import org.springframework.stereotype.Service

@Service
class UserService {
    fun getMessage(): String {
        return "userService"
    }
}
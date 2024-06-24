package com.kotlinspring.course_catalog_service.service

import org.springframework.stereotype.Service

@Service
class GreetingsService {
    fun retrieveGreeting(name: String) = "Hello $name"
}
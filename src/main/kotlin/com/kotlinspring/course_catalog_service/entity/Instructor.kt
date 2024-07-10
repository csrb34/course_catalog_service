package com.kotlinspring.course_catalog_service.entity

import jakarta.persistence.*

@Entity
@Table(name="INSTRUCTORS")
data class Instructor (
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,
    val name: String
)
package com.kotlinspring.course_catalog_service.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table


@Entity
@Table(name="Courses")
data class Course (
    val id: Int?,
    val name: String,
    val category: String
)
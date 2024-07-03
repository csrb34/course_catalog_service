package com.kotlinspring.course_catalog_service.repository

import com.kotlinspring.course_catalog_service.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int> {
}
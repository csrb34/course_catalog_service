package com.kotlinspring.course_catalog_service.repository

import com.kotlinspring.course_catalog_service.entity.Course
import org.springframework.data.repository.CrudRepository

interface CourseRepository : CrudRepository<Course, Int>{

    // JPA Query methods docs
    // https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation

    fun findByNameContaining(courseName: String): List<Course>
}

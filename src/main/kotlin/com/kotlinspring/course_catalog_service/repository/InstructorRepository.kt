package com.kotlinspring.course_catalog_service.repository

import com.kotlinspring.course_catalog_service.entity.Instructor
import org.springframework.data.repository.CrudRepository

interface InstructorRepository: CrudRepository<Instructor, Int>
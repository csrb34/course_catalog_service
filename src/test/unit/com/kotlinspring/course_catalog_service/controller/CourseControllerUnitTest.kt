package com.kotlinspring.course_catalog_service.controller

import com.kotlinspring.course_catalog_service.dto.CourseDTO
import com.kotlinspring.course_catalog_service.entity.Course
import com.kotlinspring.course_catalog_service.service.CourseService
import com.kotlinspring.course_catalog_service.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [CourseController::class])
@AutoConfigureWebTestClient
class CourseControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var courseServiceMockk: CourseService

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Learning effective communication", "Business")

        every {
            courseServiceMockk.addCourse(any())
        } returns courseDTO(id = 1)

        val saveCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertNotNull(saveCourseDTO!!.id)
    }

    @Test
    fun retrieveAllCourses(){

        every {
            courseServiceMockk.retrieveAllCourses()
        }.returnsMany(
            listOf(
                courseDTO(id = 1),
                courseDTO(id = 2, name="Learning effective communication")
            )
        )

        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, courseDTOs!!.size)
    }
//
//    @Test
//    fun updateCourse(){
//        val course = Course(null,
//            "Build RestFul APis using SpringBoot and Kotlin", "Development")
//        courseRepository.save(course)
//
//
//        val newCourseDTO = CourseDTO(null,
//            "Build microservices using Kotlin", "Development")
//
//        val updatedCourse = webTestClient
//            .put()
//            .uri("/v1/courses/{courseId}", course.id)
//            .bodyValue(newCourseDTO)
//            .exchange()
//            .expectStatus().isOk
//            .expectBody(CourseDTO::class.java)
//            .returnResult()
//            .responseBody
//
//        Assertions.assertEquals("Build microservices using Kotlin", updatedCourse!!.name)
//    }
//
//    @Test
//    fun deleteCourse(){
//        val course = Course(null,
//            "Build RestFul APis using SpringBoot and Kotlin", "Development")
//        courseRepository.save(course)
//
//        val deletedCourse = webTestClient
//            .delete()
//            .uri("/v1/courses/{courseId}", course.id)
//            .exchange()
//            .expectStatus().isNoContent
//    }
}
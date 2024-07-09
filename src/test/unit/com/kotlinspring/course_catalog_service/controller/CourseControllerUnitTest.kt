package com.kotlinspring.course_catalog_service.controller

import com.kotlinspring.course_catalog_service.dto.CourseDTO
import com.kotlinspring.course_catalog_service.service.CourseService
import com.kotlinspring.course_catalog_service.util.courseDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
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
    fun addCourse_validation() {
        val courseDTO = CourseDTO(null, "", "")

        every {
            courseServiceMockk.addCourse(any())
        } returns courseDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("courseDTO.category must not be blank, courseDTO.name must not be blank", response)
    }

    @Test
    fun addCourse_runtimeException() {
        val courseDTO = CourseDTO(null, "Learning effective communication", "Business")

        val errorMessage = "Unexpected error occurred"
        every {
            courseServiceMockk.addCourse(any())
        } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(errorMessage, response)
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

    @Test
    fun updateCourse(){
        val newCourseDTO = CourseDTO(null,
            "Build microservices using Kotlin", "Development")

        every {
            courseServiceMockk.updateCourse(any(), any())
        } returns courseDTO(id = 100, name = "Build microservices using Kotlin")

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", 100)
            .bodyValue(newCourseDTO)
            .exchange()
            .expectStatus().isOk
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("Build microservices using Kotlin", updatedCourse!!.name)
    }

    @Test
    fun deleteCourse(){

        every {
            courseServiceMockk.deleteCourse(any())
        } just runs // mocks functions that doesn't return any value

        val deletedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", 999)
            .exchange()
            .expectStatus().isNoContent
    }
}
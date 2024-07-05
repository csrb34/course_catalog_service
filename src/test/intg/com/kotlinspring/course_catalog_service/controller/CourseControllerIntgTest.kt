package com.kotlinspring.course_catalog_service.controller

import com.kotlinspring.course_catalog_service.dto.CourseDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebClient
class CourseControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun addCourse() {
        val courseDTO = CourseDTO(null, "Learning effective communication", "Business")

        val saveCourseDTO = webTestClient
            .post()
            .uri("/v1/courses")
            .bodyValue(courseDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertNotNull(saveCourseDTO?.id)
        Assertions.assertTrue{
            saveCourseDTO!!.id != null
        }
    }
}
package com.kotlinspring.course_catalog_service.controller

import com.kotlinspring.course_catalog_service.dto.InstructorDTO
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
class InstructorControllerIntgTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun createInstructor() {
        val instructorDTO = InstructorDTO(null,"Carla San Roman")

        println(instructorDTO)
        val saveInstructorDTO = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(InstructorDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertNotNull(saveInstructorDTO!!.id)
    }
}
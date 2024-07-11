package com.kotlinspring.course_catalog_service.controller

import com.kotlinspring.course_catalog_service.dto.InstructorDTO
import com.kotlinspring.course_catalog_service.service.InstructorService
import com.kotlinspring.course_catalog_service.util.instructorDTO
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [InstructorController::class])
@AutoConfigureWebTestClient
class InstructorControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var instructorServiceMockk: InstructorService

    @Test
    fun createInstructor() {
        val instructorDTO = InstructorDTO(null, "Carla San Roman")

        every {
            instructorServiceMockk.createInstructor(any())
        } returns instructorDTO(id = 1)

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

    @Test
    fun createInstructor_validation() {
        val instructorDTO = InstructorDTO(null, "")

        every {
            instructorServiceMockk.createInstructor(any())
        } returns instructorDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/instructors")
            .bodyValue(instructorDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals("instructorDTO.name must not be blank", response)
    }
}
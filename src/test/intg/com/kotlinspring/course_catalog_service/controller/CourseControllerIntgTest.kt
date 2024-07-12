package com.kotlinspring.course_catalog_service.controller

import com.kotlinspring.course_catalog_service.dto.CourseDTO
import com.kotlinspring.course_catalog_service.entity.Course
import com.kotlinspring.course_catalog_service.repository.CourseRepository
import com.kotlinspring.course_catalog_service.repository.InstructorRepository
import com.kotlinspring.course_catalog_service.util.PostgreSQLContainerInitializer
import com.kotlinspring.course_catalog_service.util.courseEntityList
import com.kotlinspring.course_catalog_service.util.instructorEntity
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.util.UriComponentsBuilder
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebClient
//@Testcontainers
class CourseControllerIntgTest : PostgreSQLContainerInitializer(){

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var courseRepository: CourseRepository

    @Autowired
    lateinit var instructorRepository: InstructorRepository

//    companion object {
//
//        @Container
//        val postgresDB = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:13-alpine")).apply {
//            withDatabaseName("testdb")
//            withUsername("postgres")
//            withPassword("secret")
//        }
//
//        @JvmStatic
//        @DynamicPropertySource
//        fun properties(registry: DynamicPropertyRegistry) {
//            registry.add("spring.datasource.url", postgresDB::getJdbcUrl)
//            registry.add("spring.datasource.username", postgresDB::getUsername)
//            registry.add("spring.datasource.password", postgresDB::getPassword)
//        }
//    }


    @BeforeEach
    fun setUp() {
        courseRepository.deleteAll()
        instructorRepository.deleteAll()

        val instructor = instructorEntity()
        instructorRepository.save(instructor)

        val courses = courseEntityList(instructor)
        courseRepository.saveAll(courses)
    }

    @Test
    fun addCourse() {
        val instructor = instructorRepository.findAll().first()

        val courseDTO = CourseDTO(null, "Learning effective communication", "Business", instructor.id)

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
        Assertions.assertTrue{
            saveCourseDTO!!.id != null
        }
    }

    @Test
    fun retrieveAllCourses(){
        val courseDTOs = webTestClient
            .get()
            .uri("/v1/courses")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

//        println("CourseDTOs: $courseDTOs")
        Assertions.assertEquals(3, courseDTOs!!.size)
    }

    @Test
    fun retrieveAllCourses_ByName(){

        val uri = UriComponentsBuilder
            .fromUriString("/v1/courses")
            .queryParam("course_name", "SpringBoot")
            .toUriString()

        val courseDTOs = webTestClient
            .get()
            .uri(uri)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(CourseDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertEquals(2, courseDTOs!!.size)
    }

    @Test
    fun updateCourse(){
        val instructor = instructorRepository.findAll().first()

        val course = Course(null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development", instructor)
        courseRepository.save(course)


        val newCourseDTO = CourseDTO(null,
            "Build microservices using Kotlin", "Development", instructor.id)

        val updatedCourse = webTestClient
            .put()
            .uri("/v1/courses/{courseId}", course.id)
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
        val instructor = instructorRepository.findAll().first()

        val course = Course(null,
            "Build RestFul APis using SpringBoot and Kotlin", "Development", instructor)
        courseRepository.save(course)

        val deletedCourse = webTestClient
            .delete()
            .uri("/v1/courses/{courseId}", course.id)
            .exchange()
            .expectStatus().isNoContent
    }
}
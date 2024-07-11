package com.kotlinspring.course_catalog_service.service

import com.kotlinspring.course_catalog_service.dto.CourseDTO
import com.kotlinspring.course_catalog_service.entity.Course
import com.kotlinspring.course_catalog_service.entity.Instructor
import com.kotlinspring.course_catalog_service.exception.CourseNotFoundException
import com.kotlinspring.course_catalog_service.exception.InstructorNotValidException
import com.kotlinspring.course_catalog_service.repository.CourseRepository
import mu.KLogging
import org.hibernate.annotations.NotFound
import org.springframework.stereotype.Service

@Service
class CourseService(
    val courseRepository: CourseRepository,
    val instructorService: InstructorService
) {

    companion object: KLogging()

    fun addCourse(courseDTO: CourseDTO): CourseDTO{

        val maybeInstructor = instructorService.findByInstructorId(courseDTO.instructorId!!)

        if (!maybeInstructor.isPresent){
            throw InstructorNotValidException("Instructor ID ${courseDTO.instructorId} is NOT valid")
        }

        val courseEntity = courseDTO.let {
            Course(null, it.name, it.category, maybeInstructor.get())
        }

        courseRepository.save(courseEntity)
        logger.info("Saved course is: $courseEntity")

        return courseEntity.let {
            CourseDTO(it.id, it.name, it.category, it.instructor!!.id)
        }
    }

    fun retrieveAllCourses(courseName: String?): List<CourseDTO> {
        val courses = courseName?.let {
            courseRepository.findCoursesByName(courseName)
        } ?: courseRepository.findAll()

        return courses.map {
            CourseDTO(it.id, it.name, it.category)
        }
    }

    fun updateCourse(courseId: Int, courseDTO: CourseDTO): CourseDTO {
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent){
            existingCourse.get()
                .let {
                    it.name = courseDTO.name
                    it.category = courseDTO.category
                    courseRepository.save(it)
                    CourseDTO(it.id, it.name, it.category)
                }
        }else{
            throw CourseNotFoundException("No course found with ID: $courseId")
        }
    }

    fun deleteCourse(courseId: Int){
        val existingCourse = courseRepository.findById(courseId)

        return if (existingCourse.isPresent){
            existingCourse.get()
                .let {
                    courseRepository.deleteById(courseId)
                }
        }else{
            throw CourseNotFoundException("No course found with ID: $courseId")
        }
    }
}

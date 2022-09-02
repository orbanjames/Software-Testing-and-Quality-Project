package com.jamesorban.softwaretestingproject.controllers;

import com.jamesorban.softwaretestingproject.dao.CourseRepository;
import com.jamesorban.softwaretestingproject.model.Course;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/course")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @GetMapping
    public List<Course> getAllCourseRecords(){

        return courseRepository.findAll();
    }

    @GetMapping(value = "{courseId}")
    public Course getCourseById(@PathVariable(value = "courseId") Long courseId){
        return courseRepository.findById(courseId).get();
    }

    @PostMapping
    public Course createCourseRecord(@RequestBody @Validated Course courseRecord){

        return courseRepository.save(courseRecord);
    }

    @PutMapping
    public Course updateCourseRecord(@RequestBody @Validated Course courseRecord) throws NotFoundException {
        if(courseRecord == null || courseRecord.getCourseId() == null) {
            throw new NotFoundException("CourseRecord or ID must not be null");
        }
        Optional<Course>optionalCourse=courseRepository.findById(courseRecord.getCourseId());
        if(!optionalCourse.isPresent()){
            throw new NotFoundException("Course with ID:"+ courseRecord.getCourseId()+"does not exist");
        }
        Course existingCourseRecord = optionalCourse.get();
        existingCourseRecord.setCourseName(courseRecord.getCourseName());
        existingCourseRecord.setCourseCode(courseRecord.getCourseCode());
        existingCourseRecord.setEnrolledStudentId(courseRecord.getEnrolledStudentId());
        return courseRepository.save(existingCourseRecord);
    }

    @DeleteMapping(value = "{facultyId}")
    public void deleteFacultyById(@PathVariable(value = "facultyId") Long facultyId) throws NotFoundException{
        if(!courseRepository.findById(facultyId).isPresent()){
            throw new NotFoundException("facultyId" +facultyId + "not present");
        }
        courseRepository.deleteById(facultyId);
    }
}

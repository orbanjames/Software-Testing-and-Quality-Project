package com.jamesorban.softwaretestingproject.services;

import com.jamesorban.softwaretestingproject.dao.CourseRepository;
import com.jamesorban.softwaretestingproject.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepository courseRepository;


    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public List<Course> EnrolledStudents() {
        return courseRepository.saveAll();
    }


    public Course findByCourseById(long courseId) {
        return courseRepository.findByCourseById(courseId);
    }


    public void delete(Course course) {
        courseRepository.delete(course);

    }

}
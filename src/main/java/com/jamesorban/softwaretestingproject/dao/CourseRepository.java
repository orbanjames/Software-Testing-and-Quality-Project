package com.jamesorban.softwaretestingproject.dao;

import com.jamesorban.softwaretestingproject.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Object findByIdIfNotExist(Long CourseId);

    List<Course> saveAll();

    Course findByCourseById(long courseId);
}

package com.jamesorban.softwaretestingproject.dao;

import com.jamesorban.softwaretestingproject.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {

    Object findByIdNotExist(Long studentId);
}

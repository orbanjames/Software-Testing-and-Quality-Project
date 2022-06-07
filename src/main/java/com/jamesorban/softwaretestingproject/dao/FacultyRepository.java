package com.jamesorban.softwaretestingproject.dao;

import com.jamesorban.softwaretestingproject.model.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    Object findByIdIfNotExist(Long facultyId);
}

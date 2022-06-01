package com.jamesorban.softwaretestingproject.dao;

import com.jamesorban.softwaretestingproject.model.University;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UniversityRepository extends JpaRepository<University, Long> {
}

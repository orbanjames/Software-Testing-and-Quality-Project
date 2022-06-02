package com.jamesorban.softwaretestingproject.controllers;

import com.jamesorban.softwaretestingproject.dao.FacultyRepository;
import com.jamesorban.softwaretestingproject.model.Faculty;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/faculty")
public class FacultyController {

    @Autowired
    FacultyRepository facultyRepository;

    @GetMapping
    public List<Faculty> getAllFacultyRecords(){

        return facultyRepository.findAll();
    }

    @GetMapping(value = "{facultyId}")
    public Faculty getFacultyById(@PathVariable(value = "facultyId") Long facultyId){
        return facultyRepository.findById(facultyId).get();
    }

    @PostMapping
    public Faculty createFacultyRecord(@RequestBody @Validated Faculty facultyRecord){

        return facultyRepository.save(facultyRecord);
    }

    @PutMapping
    public Faculty updateFacultyRecord(@RequestBody @Validated Faculty facultyRecord) throws NotFoundException {
        if(facultyRecord == null || facultyRecord.getFacultyId() == null) {
            throw new NotFoundException("FacultyRecord or ID must not be null");
        }
        Optional<Faculty>optionalFaculty=facultyRepository.findById(facultyRecord.getFacultyId());
        if(!optionalFaculty.isPresent()){
            throw new NotFoundException("Faculty with ID:"+ facultyRecord.getFacultyId()+"does not exist");
        }
        Faculty existingFacultyRecord = optionalFaculty.get();
        existingFacultyRecord.setName(facultyRecord.getName());
        existingFacultyRecord.setLocation(facultyRecord.getLocation());
        existingFacultyRecord.setAddress(facultyRecord.getAddress());
        existingFacultyRecord.setUniId(facultyRecord.getUniId());
        return facultyRepository.save(existingFacultyRecord);
    }

    @DeleteMapping(value = "{facultyId}")
    public void deleteFacultyById(@PathVariable(value = "facultyId") Long facultyId) throws NotFoundException{
        if(!facultyRepository.findById(facultyId).isPresent()){
            throw new NotFoundException("facultyId" +facultyId + "not present");
        }
        facultyRepository.deleteById(facultyId);
    }
}

package com.jamesorban.softwaretestingproject.controllers;

import com.jamesorban.softwaretestingproject.dao.UniversityRepository;
import com.jamesorban.softwaretestingproject.model.University;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/university")
public class UniversityController {

    @Autowired
    UniversityRepository universityRepository;

    @GetMapping
    public List<University> getAllUniversityRecords(){

        return universityRepository.findAll();
    }

    @GetMapping(value = "{uniId}")
    public University getUniversityById(@PathVariable(value = "uniId") Long studentId){
        return universityRepository.findById(studentId).get();
    }

    @PostMapping
    public University createUniversityRecord(@RequestBody @Validated University universityRecord){

        return universityRepository.save(universityRecord);
    }

    @PutMapping
    public University updateUniversityRecord(@RequestBody @Validated University universityRecord) throws NotFoundException {
        if(universityRecord == null || universityRecord.getUniId() == null) {
            throw new NotFoundException("UniversityRecord or ID must not be null");
        }
        Optional<University>optionalUniversity=universityRepository.findById(universityRecord.getUniId());
        if(!optionalUniversity.isPresent()){
            throw new NotFoundException("University with ID:"+ universityRecord.getUniId()+"does not exist");
        }
        University existingUniversityRecord = optionalUniversity.get();
        existingUniversityRecord.setName(universityRecord.getName());
        existingUniversityRecord.setLocation(universityRecord.getLocation());
        existingUniversityRecord.setAddress(universityRecord.getAddress());
        existingUniversityRecord.setRanking(universityRecord.getRanking());
        return universityRepository.save(existingUniversityRecord);
    }

    @DeleteMapping(value = "{uniId}")
    public void deleteUniversityById(@PathVariable(value = "uniId") Long uniId) throws NotFoundException{
        if(!universityRepository.findById(uniId).isPresent()){
            throw new NotFoundException("uniId" +uniId + "not present");
        }
        universityRepository.deleteById(uniId);
    }
}

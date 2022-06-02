package com.jamesorban.softwaretestingproject.controllers;

import com.jamesorban.softwaretestingproject.dao.StudentRepository;
import com.jamesorban.softwaretestingproject.model.Student;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    StudentRepository studentRepository;

    @GetMapping
    public List<Student> getAllStudentRecords(){

        return studentRepository.findAll();
    }

    @GetMapping(value = "{studentId}")
    public Student getStudentById(@PathVariable(value = "studentId") Long studentId){
        return studentRepository.findById(studentId).get();
    }

    @PostMapping
    public Student createStudentRecord(@RequestBody @Validated Student studentRecord){

        return studentRepository.save(studentRecord);
    }

    @PutMapping
    public Student updateStudentRecord(@RequestBody @Validated Student studentRecord) throws NotFoundException {
        if(studentRecord == null || studentRecord.getStudentId() == null) {
            throw new NotFoundException("StudentRecord or ID must not be null");
        }
        Optional<Student>optionalStudent=studentRepository.findById(studentRecord.getStudentId());
        if(!optionalStudent.isPresent()){
            throw new NotFoundException("Student with ID:"+ studentRecord.getStudentId()+"does not exist");
        }
        Student existingStudentRecord = optionalStudent.get();
        existingStudentRecord.setFirstName(studentRecord.getFirstName());
        existingStudentRecord.setLastName(studentRecord.getLastName());
        existingStudentRecord.setEmail(studentRecord.getEmail());
        existingStudentRecord.setCountry(studentRecord.getCountry());
        existingStudentRecord.setProgramme(studentRecord.getProgramme());
        existingStudentRecord.setContact(studentRecord.getContact());
        existingStudentRecord.setYearAwarded(studentRecord.getYearAwarded());
        return studentRepository.save(existingStudentRecord);
    }

    @DeleteMapping(value = "{studentId}")
    public void deleteStudentById(@PathVariable(value = "studentId") Long studentId) throws NotFoundException{
        if(!studentRepository.findById(studentId).isPresent()){
            throw new NotFoundException("studentId" +studentId + "not present");
        }
        studentRepository.deleteById(studentId);
    }
}

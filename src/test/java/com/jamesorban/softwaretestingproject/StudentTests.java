package com.jamesorban.softwaretestingproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jamesorban.softwaretestingproject.controllers.StudentController;
import com.jamesorban.softwaretestingproject.dao.StudentRepository;
import com.jamesorban.softwaretestingproject.model.Student;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(MockitoJUnitRunner.class)
public class StudentTests {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentController studentController;



    Student RECORD_1 = new Student(1L, "Aondowase", "Orban","orban@gmail.com", "Nigeria", "M.SC", "08189427322",2021);
    Student RECORD_2 = new Student(2L, "James", "Aondowase","james@gmail.com", "Ghana", "PHD", "+253648653",2020);
    Student RECORD_3 = new Student(3L, "Lilian", "Odon","lilian@gmail.com", "South Africa", "B.SC", "+45689427322",2018);

    public Student getRECORD_1() {
        return RECORD_1;
    }

    public void setRECORD_1(Student RECORD_1) {
        this.RECORD_1 = RECORD_1;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void getAllStudentRecords_success() throws Exception{
        List<Student> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        Mockito.when(studentRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].firstName", is("Aondowase")));
    }


    @Test
    public void getStudentById_success() throws Exception{
        Mockito.when(studentRepository.findById(RECORD_1.getStudentId())).thenReturn(java.util.Optional.of(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.firstName", is("Aondowase")));
    }

    @Test
    public void createRecord_success() throws Exception{
        Student record = Student.builder()
                .studentId(4L)
                .firstName("Gabriel")
                .lastName("Isibor")
                .email("gabriel@yahoo.com")
                .country("Congo")
                .programme("PhD")
                .contact("+23468744537")
                .yearAwarded(2022)
                .build();
        Mockito.when(studentRepository.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.email", is("gabriel@yahoo.com")));
    }

    @Test
    public void updateStudentRecord_success() throws Exception{
        Student updatedRecord = Student.builder()
                .studentId(2L)
                .firstName("Updated Student FirstName")
                .lastName("Updated Student Lastname")
                .email("Updated Student Email")
                .country("Updated Student Country")
                .programme("Updated Student Programme")
                .contact("Updated Student Contact")
                .yearAwarded(2020)
                .build();

        Mockito.when(studentRepository.findById(RECORD_2.getStudentId())).thenReturn(java.util.Optional.ofNullable(RECORD_2));
        Mockito.when(studentRepository.save(updatedRecord)).thenReturn(updatedRecord);

        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.programme", is("Updated Student Programme")));
    }

    @Test
    public void deleteStudentById_success() throws Exception{
        Mockito.when(studentRepository.findById(RECORD_3.getStudentId())).thenReturn(Optional.of(RECORD_3));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

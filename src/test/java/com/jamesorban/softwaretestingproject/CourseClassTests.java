package com.jamesorban.softwaretestingproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jamesorban.softwaretestingproject.controllers.CourseController;
import com.jamesorban.softwaretestingproject.dao.CourseRepository;
import com.jamesorban.softwaretestingproject.model.Course;
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
public class CourseClassTests{
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseController courseController;



    Course RECORD_1 = new Course(1L, "M9004", "Software Testing and Quality", 1);
    Course RECORD_2 = new Course(2L, "M9035", "Software Process", 2);
    Course RECORD_3 = new Course(3L, "M9044", "Advanced Software Technologies", 4);
    Course RECORD_4 = new Course(4L, "M9333", "Software Construction", 7);
    Course RECORD_5 = new Course(5L, "M9255", "Tools and Analysis of Software Engineering", 5);

    public Course getRECORD_1() {
        return RECORD_1;
    }

    public void setRECORD_1(Course RECORD_1) {
        this.RECORD_1 = RECORD_1;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(courseController).build();
    }

    @Test
    public void getAllCourseRecords() throws Exception{
        List<Course> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3, RECORD_4, RECORD_5));
        Mockito.when(courseRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/course")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(5)))
                .andExpect(jsonPath("$[2].CourseName", is("Software Process")));
    }


    @Test
    public void getCourseByIdExist() throws Exception{
        Mockito.when(courseRepository.findById(RECORD_5.getCourseId())).thenReturn(java.util.Optional.of(RECORD_5));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/course/5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.CourseCode", is("M9333")));
    }

    @Test
    public void getCourseByIdNotFound() throws Exception{
        Mockito.when(courseRepository.findByIdIfNotExist(RECORD_2.getCourseId())).thenReturn(java.util.Optional.of(RECORD_2));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/course/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.CourseName", is("Software Requirement and Modelling")));
    }


    @Test
    public void createRecord() throws Exception{
        Course record = Course.builder()
                .CourseId(3L)
                .CourseName("Advanced Software Technologies")
                .CourseCode("M9044")
                .EnrolledStudentId(4)
                .build();
        Mockito.when(courseRepository.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.CourseName", is("Advanced Software Technologies")));
    }

    @Test
    public void updateCourseRecord() throws Exception{
        Course updatedRecord = Course.builder()
                .CourseId(2L)
                .CourseName("Updated course Name")
                .CourseCode("Updated course code")
                .EnrolledStudentId(2)
                .build();
        Mockito.when(courseRepository.findById(RECORD_2.getCourseId())).thenReturn(Optional.ofNullable(RECORD_2));
        Mockito.when(courseRepository.save(updatedRecord)).thenReturn(updatedRecord);

        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.CourseName", is("Software Process")));
    }

    @Test
    public void deleteCourseById() throws Exception{
        Mockito.when(courseRepository.findById(RECORD_1.getCourseId())).thenReturn(Optional.of(RECORD_1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/course/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

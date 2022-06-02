package com.jamesorban.softwaretestingproject;

import com.jamesorban.softwaretestingproject.controllers.UniversityController;
import com.jamesorban.softwaretestingproject.dao.UniversityRepository;
import com.jamesorban.softwaretestingproject.model.University;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
public class UniversityTests {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private UniversityRepository universityRepository;

    @InjectMocks
    private UniversityController universityController;



    University RECORD_1 = new University(1L, "University of Belgrade", "Belgrade","Belgrade, Serbia", 1);
    University RECORD_2 = new University(2L, "University of Novi Sad", "Novi Sad","Novi Sad, Serbia", 2);
    University RECORD_3 = new University(3L, "University of Nis", "Nis","Nis, Serbia", 3);

    public University getRECORD_1() {
        return RECORD_1;
    }

    public void setRECORD_1(University RECORD_1) {
        this.RECORD_1 = RECORD_1;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(universityController).build();
    }

    @Test
    public void getAllUniversityRecords_success() throws Exception{
        List<University> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        Mockito.when(universityRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/university")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].name", is("University of Nis")));
    }


    @Test
    public void getStudentById_success() throws Exception{
        Mockito.when(universityRepository.findById(RECORD_1.getUniId())).thenReturn(java.util.Optional.of(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/university/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("University of Belgrade")));
    }

    @Test
    public void createRecord_success() throws Exception{
        University record = University.builder()
                .uniId(4L)
                .name("Metropolitan University")
                .location("Sports and Recreational Center, Milan Gale Muskatirovic")
                .address("Tadeusa Koscuska 63, Beograd 11158")
                .ranking(7)
                .build();
        Mockito.when(universityRepository.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/university")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.address", is("Tadeusa Koscuska 63, Beograd 11158")));
    }

    @Test
    public void updateUniversityRecord_success() throws Exception{
        University updatedRecord = University.builder()
                .uniId(3L)
                .name("Updated University Name")
                .location("Updated University Location")
                .address("Updated University Address")
                .ranking(3)
                .build();

        Mockito.when(universityRepository.findById(RECORD_3.getUniId())).thenReturn(java.util.Optional.ofNullable(RECORD_3));
        Mockito.when(universityRepository.save(updatedRecord)).thenReturn(updatedRecord);

        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/university")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.location", is("Updated University Location")));
    }

    @Test
    public void deleteUniversityById_success() throws Exception{
        Mockito.when(universityRepository.findById(RECORD_2.getUniId())).thenReturn(Optional.of(RECORD_2));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/university/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

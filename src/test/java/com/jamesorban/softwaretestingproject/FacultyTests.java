package com.jamesorban.softwaretestingproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jamesorban.softwaretestingproject.controllers.FacultyController;
import com.jamesorban.softwaretestingproject.dao.FacultyRepository;
import com.jamesorban.softwaretestingproject.model.Faculty;
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
public class FacultyTests {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private FacultyRepository facultyRepository;

    @InjectMocks
    private FacultyController facultyController;



    Faculty RECORD_1 = new Faculty(1L, "Faculty of Organizational Sciences", "Jove ilica 154,","Belgrade, Serbia", 1);
    Faculty RECORD_2 = new Faculty(2L, "Faculty of Natural and Applied Sciences", "Jovanana Milanovic Hospital Road","Novi Sad, Serbia", 2);
    Faculty RECORD_3 = new Faculty(3L, "Faculty of Political Science and International Relations", "Sava Center, New Belgrade","Belgrade, Serbia", 4);

    public Faculty getRECORD_1() {
        return RECORD_1;
    }

    public void setRECORD_1(Faculty RECORD_1) {
        this.RECORD_1 = RECORD_1;
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(facultyController).build();
    }

    @Test
    public void getAllFacultyRecords_success() throws Exception{
        List<Faculty> records = new ArrayList<>(Arrays.asList(RECORD_1, RECORD_2, RECORD_3));
        Mockito.when(facultyRepository.findAll()).thenReturn(records);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[2].address", is("Sava Center, New Belgrade")));
    }


    @Test
    public void getFacultyById_success() throws Exception{
        Mockito.when(facultyRepository.findById(RECORD_1.getFacultyId())).thenReturn(java.util.Optional.of(RECORD_1));
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Faculty of Organizational Sciences")));
    }

    @Test
    public void createRecord_success() throws Exception{
        Faculty record = Faculty.builder()
                .facultyId(4L)
                .name("Faculty of Mathematics and Computer Sciences")
                .location("Sports and Recreational Center")
                .address("Tadeusa Koscuska 63, Beograd 11158")
                .uniId(1)
                .build();
        Mockito.when(facultyRepository.save(record)).thenReturn(record);

        String content = objectWriter.writeValueAsString(record);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.location", is("Sports and Recreational Center")));
    }

    @Test
    public void updateFacultyRecord_success() throws Exception{
        Faculty updatedRecord = Faculty.builder()
                .facultyId(2L)
                .name("Updated Faculty Name")
                .location("Updated Faculty Location")
                .address("Updated Faculty Address")
                .uniId(2)
                .build();

        Mockito.when(facultyRepository.findById(RECORD_2.getFacultyId())).thenReturn(java.util.Optional.ofNullable(RECORD_2));
        Mockito.when(facultyRepository.save(updatedRecord)).thenReturn(updatedRecord);

        String updatedContent = objectWriter.writeValueAsString(updatedRecord);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.put("/faculty")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(updatedContent);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Updated Faculty Name")));
    }

    @Test
    public void deleteFacultyById_success() throws Exception{
        Mockito.when(facultyRepository.findById(RECORD_1.getFacultyId())).thenReturn(Optional.of(RECORD_1));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


}

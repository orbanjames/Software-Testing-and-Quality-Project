package com.jamesorban.softwaretestingproject.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long CourseId;

    @NotNull
    private String CourseCode;

    @NotNull
    private String CourseName;

    @NotNull
    private Integer EnrolledStudentId;

}
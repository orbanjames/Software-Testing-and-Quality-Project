package com.jamesorban.softwaretestingproject.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "faculty")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long facultyId;

    @NotNull
    private String name;

    @NotNull
    private String address;

    @NotNull
    private String location;

    @NotNull
    private int uniId;

}
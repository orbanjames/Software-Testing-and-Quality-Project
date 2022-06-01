package com.jamesorban.softwaretestingproject.model;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "university")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long uniId;

    @NotNull
    private String name;

    @NotNull
    private String location;

    @NotNull
    private String address;

    @NotNull
    private int ranking;

}
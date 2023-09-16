package com.example.kursavoy.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Forgingroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_forgin_group")
    private int idForginGroup;
    @Basic
    @Column(name = "language")
    private String language;
    @Basic
    @Column(name = "col_student")
    private int colStudent;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private  Admim admin;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "forgingroup")
    private List<Students> students = new ArrayList<>();


}

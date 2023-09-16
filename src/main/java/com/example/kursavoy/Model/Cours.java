package com.example.kursavoy.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "cours")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cours {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_cours")
    private int idCours;
    @Basic
    @Column(name = "NameCours")
    private String nameCours;
    @Basic
    @Column(name = "level_for_Cours")
    private String levelForCours;
    @Basic
    @Column(name = "info_about_cours", length = 1000)
    private String infoCours;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Forgingroup forgingroup;


}

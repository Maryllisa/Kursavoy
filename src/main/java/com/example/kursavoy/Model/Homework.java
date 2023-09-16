package com.example.kursavoy.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Calendar;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "homework")
public class Homework {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_homework")
    private Long idHomework;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Students studentsByIdPerson;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private  Document document;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Work work;
    @Basic
    @Column(name = "mark")
    private int mark;

  }

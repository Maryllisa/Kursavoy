package com.example.kursavoy.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Blog {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_blog")
    private long id_blog;

    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "info")
    private String info;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private  Document document;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private  Forgingroup forgingroup;


}

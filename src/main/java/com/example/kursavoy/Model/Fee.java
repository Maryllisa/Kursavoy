package com.example.kursavoy.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Arrays;
import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fee {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id_fee")
    private long idFee;
    @Basic
    @Column(name = "date")
    private Date date;
    @Basic
    @Column(name = "status_fee")
    private boolean status_fee;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private  Document document;

    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Students studentsByIdStudent;


}

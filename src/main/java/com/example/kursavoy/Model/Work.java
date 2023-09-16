package com.example.kursavoy.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Work {
    @Id
    @Column(name = "date")
    private Date date;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "type")
    private String type;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER)
    private Forgingroup forgingroup;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "work")
    private List<Homework> homework = new ArrayList<>();
    public void addHomework(Homework homework)
    {
        int flag=0;
        for (int i = 0; i < this.homework.size(); i++) {
            if(this.homework.get(i).getIdHomework()== homework.getIdHomework()) {
                this.homework.get(i).setMark(homework.getMark());
                flag++;
            }
        }
        if (flag==0) {
            homework.setWork(this);
            this.homework.add(homework);
        }
    }
}

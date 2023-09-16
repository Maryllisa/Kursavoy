package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, Long> {
    @Query("SELECT f FROM Fee f " +
            "WHERE f.studentsByIdStudent.forgingroup.idForginGroup = ?1")
    List<Fee> findAllByGroup(int i);
    @Query("SELECT f FROM Fee f " +
            "WHERE f.studentsByIdStudent.idStudents = ?1")
    Iterable<Fee> findByStudent(int idStudents);
    @Query("SELECT f FROM Fee f " +
            "WHERE f.studentsByIdStudent.idStudents = ?1" +
            " order by f.date asc ")
    Iterable<Fee> findByStudentByDateAsc(int idStudents);
    @Query("SELECT f FROM Fee f " +
            "WHERE f.studentsByIdStudent.idStudents = ?1" +
            " order by f.date desc  ")
    Iterable<Fee> findByStudentByDateDesc(int idStudents);
    @Query("SELECT f FROM Fee f " +
            "WHERE f.studentsByIdStudent.forgingroup.idForginGroup = ?1 and f.studentsByIdStudent.surname like %?2% and f.studentsByIdStudent.name like %?3%" +
            " and f.studentsByIdStudent.patronymic like %?4%")
    List<Fee> findAllByGroupBySNP(int i, String surname, String name, String patronymic);
}

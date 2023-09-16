package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Homework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    @Query("SELECT hm FROM Homework hm " +
            "INNER JOIN Students s on hm.studentsByIdPerson.idStudents = s.idStudents " +
            "INNER JOIN Forgingroup f on s.forgingroup.idForginGroup = f.idForginGroup " +
            "INNER join Admim a on f.admin.idAdmin = a.idAdmin " +
            "WHERE a.idAdmin = ?1")
    public Iterable<Homework> getHomeworkByAdmin(int id);
    @Query("SELECT hm FROM Homework hm " +
            "Where hm.studentsByIdPerson.idStudents =?1 and hm.work.date =?2")
    Homework findByIdStudentAndWork(int id, Date date);
}

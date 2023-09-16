package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Homework;
import com.example.kursavoy.Model.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkRepository extends JpaRepository<Work, Date> {

@Query("select w from Work w " +
        "WHERE w.forgingroup.idForginGroup =?1")
    Iterable<Work> findWorkByGroup(int id);
    @Query("select w from Work w " +
            "WHERE w.forgingroup.idForginGroup =?1")
    List<Work> findWorkByGroupSort(int id);
@Query("SELECT w from Work w " +
        "INNER JOIN Students st on st.forgingroup.idForginGroup = ?5 " +
        "WHERE st.surname like %?1% and st.name like %?2% and st.patronymic like %?3% and st.brth like %?4%")
    List<Work> findAllByGroupAndSNP(String surname, String name, String patronymic, String brth, int id);
@Query("SELECT w FROM Work w " +
        " where w.forgingroup.idForginGroup=?1 ")
    List<Work> findByGroup(int idForginGroup);

@Query("SELECT wr FROM Work wr " +
        "WHERE wr.forgingroup.idForginGroup = ?1")
    Iterable<Work> findByIdGroup(int idStudents);
    @Query("SELECT c FROM Work c " +
            "INNER JOIN Forgingroup f on c.forgingroup.idForginGroup = f.idForginGroup " +
            "INNER join Admim a on f.admin.idAdmin = a.idAdmin " +
            "WHERE a.idAdmin = ?1")
    public Iterable<Work> getHomeworkByAdmin(int id);
    @Query("SELECT wr FROM Work wr " +
            "WHERE wr.forgingroup.idForginGroup = ?1 " +
            "order by wr.date desc")
    Iterable<Work> findByIdGroupSortByDate(int idForginGroup);
    @Query("SELECT wr FROM Work wr " +
            "INNER JOIN Homework hm ON hm.studentsByIdPerson.forgingroup.idForginGroup = wr.forgingroup.idForginGroup" +
            " WHERE wr.forgingroup.idForginGroup = ?1 " +
            "order by hm.mark desc ")
    Iterable<Work> findByIdGroupSortByMark(int idForginGroup);

}

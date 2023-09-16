package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Cours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CoursRepository extends JpaRepository<Cours, Integer> {
    @Query("SELECT cr FROM cours cr " +
            "WHERE cr.forgingroup.admin.idAdmin is null")
    Iterable<Cours> findByWithoutAdmin();

    @Query("SELECT cr FROM cours cr " +
            "WHERE cr.forgingroup.admin.idAdmin = ?1")
    Cours findByGroupAndStudent(int id);

    @Query("Select c From cours c "+
            "Inner join Forgingroup f On c.forgingroup.idForginGroup = f.idForginGroup "+
            "WHERE f.admin.idAdmin = ?1")
    List<Cours> findByAdmin(int id);
    @Query("Select c From cours c "+
            "WHERE c.forgingroup.idForginGroup = ?1")
    Optional<Cours> findByGroup(int id);
    @Query("SELECT cr FROM cours cr " +
            "INNER JOIN Students st On st.forgingroup.idForginGroup " +
            "= cr.forgingroup.idForginGroup " +
            "WHERE st.forgingroup.idForginGroup = ?5 and " +
            "(st.surname LIKE ?1 or  st.name LIKE ?2 or st.name LIKE ?3 or st.brth = ?4)")
    Iterable<Cours> finderByParam(String surname, String name, String patronymic, String brth, int id);
}

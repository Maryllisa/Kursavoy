package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Forgingroup;
import com.example.kursavoy.Model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Students, Integer> {
    Students findByLogin(String login);
    @Query("SELECT st FROM Students st " +
            "INNER JOIN Forgingroup fr ON st.forgingroup.idForginGroup = fr.idForginGroup " +
            "where st.forgingroup.idForginGroup = ?1")
    List<Students> findByGroup(int id);
    @Query("SELECT st FROM Students st " +
            "WHERE st.forgingroup.idForginGroup is null")
    List<Students> findStudentsWithoutGroup();
    @Query("SELECT st FROM Students st " +
            "WHERE st.forgingroup.idForginGroup is not null")
    List<Students> findStudentsWithGroup();

    @Query("SELECT s FROM Students s " +
            "WHERE s.surname like %?1% and s.name like %?2% and s.patronymic like %?3% and s.brth like %?4% and s.forgingroup.idForginGroup is not null")
    List<Students> findBySNPAndB(String surname, String name, String patronymic, String brth);
    @Query("SELECT st FROM Students st " +
            "WHERE st.forgingroup.idForginGroup = ?1")
    List<Students> findAllByGroup(int i);

    Students findByActivationCode(String code);
    @Query("SELECT s FROM Students s " +
            "WHERE s.email =?1")
    Optional<Students> findByEmail(String email);
    @Query("SELECT s FROM Students s " +
            "WHERE s.reset_password_token =?1")
    Optional<Students> findByReset_password_token(String token);
}

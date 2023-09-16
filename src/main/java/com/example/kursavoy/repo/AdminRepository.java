package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admim, Integer> {
    Admim findByLogin(String login);
    @Query("SELECT ad FROM Admim ad " +
            "WHERE ad.forgingroup.size = 0")
    List<Admim> findStudentsWithoutGroup();

    Admim findByActivationCode(String code);
    @Query("SELECT a FROM Admim a " +
            "WHERE a.eMail =?1")
    Optional<Admim> findByEmail(String email);
    @Query("SELECT a FROM Admim a " +
            "WHERE a.reset_password_token =?1")
    Optional<Admim> findByReset_password_token(String token);
}

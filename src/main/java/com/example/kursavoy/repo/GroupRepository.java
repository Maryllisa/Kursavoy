package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Cours;
import com.example.kursavoy.Model.Forgingroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Forgingroup, Integer> {

    @Query("SELECT g FROM Forgingroup g " +
            "WHERE g.admin.idAdmin = ?1")
    List<Forgingroup> findByAdmin(int idAdmin);
}

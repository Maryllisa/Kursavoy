package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM  Blog b " +
            "WHERE b.forgingroup.idForginGroup =?1")
    List<Blog> findAllByGroup(int i);
@Query("SELECT b FROM Blog b " +
        "WHERE b.info =?2 and b.title =?1")
    Blog findByTitleAndInfo(String title, String info);
    @Query("SELECT b FROM  Blog b " +
            "WHERE b.forgingroup.idForginGroup =?1" +
            " order by b.title asc ")
    Iterable<Blog> findAllByGroupFromA(int idForginGroup);
    @Query("SELECT b FROM  Blog b " +
            "WHERE b.forgingroup.idForginGroup =?1" +
            " order by b.title desc ")
    Iterable<Blog> findAllByGroupToA(int idForginGroup);
    @Query("SELECT b FROM Blog b " +
            "WHERE b.title like %?2% and b.forgingroup.idForginGroup =?1")

    Iterable<Blog> findAllByGroupSearch(int idForginGroup, String title);
    @Query("SELECT b FROM Blog b " +
            "WHERE b.title like %?1%")
    Iterable<Blog> findAllByGroupSearch(String title);
    @Query("SELECT b FROM  Blog b " +
            " order by b.title desc ")
    Iterable<Blog> findAllByGroupToA();
    @Query("SELECT b FROM  Blog b " +
            " order by b.title asc ")
    Iterable<Blog> findAllByGroupFromA();
}

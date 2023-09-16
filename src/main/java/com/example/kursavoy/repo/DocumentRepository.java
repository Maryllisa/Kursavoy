package com.example.kursavoy.repo;

import com.example.kursavoy.Model.Blog;
import com.example.kursavoy.Model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DocumentRepository extends JpaRepository<Document, Long> {

}

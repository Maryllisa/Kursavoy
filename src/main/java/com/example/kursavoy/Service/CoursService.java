package com.example.kursavoy.Service;

import com.example.kursavoy.Model.Cours;
import com.example.kursavoy.repo.CoursRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CoursService {
    private final CoursRepository coursRepository;
    public Iterable<Cours> readCours()
    {
        Iterable<Cours> cours = coursRepository.findAll();
        return cours;
    }
    public Cours readCours(int id)
    {
        Cours cours = coursRepository.findById(id).orElse(null);
        return cours;
    }
    public Iterable<Cours> findCoursWithoutAdmin()
    {
        return coursRepository.findByWithoutAdmin();
    }

    public List<Cours> readCoursAdmin(int id) {
        return coursRepository.findByAdmin(id);
    }

    public Cours readCoursByGroup(int id) {
        return coursRepository.findByGroup(id).orElse(null);
    }

    public Iterable<Cours> finderForStudent(String surname, String name, String patronymic, String brth, int group) {
    return coursRepository.finderByParam(surname, name,patronymic, brth, group);
    }

    public Cours findCoursByGroup(int idForginGroup) {
        return coursRepository.findByGroup(idForginGroup).orElse(null);
    }
}


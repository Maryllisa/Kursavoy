package com.example.kursavoy.Service;

import com.example.kursavoy.Model.Homework;
import com.example.kursavoy.Model.Work;
import com.example.kursavoy.repo.HomeworkRepository;
import com.example.kursavoy.repo.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final WorkRepository workRepository;

    public Iterable<Homework> readHomewokByAdmin(int idAdmin) {
    return homeworkRepository.getHomeworkByAdmin(idAdmin);
    }

    public void addMark(Long id, int parseInt, Date date) {
        Homework homeworkFromDB = homeworkRepository.findById(id).orElse(null);
        homeworkFromDB.setMark(parseInt);
        Work workFromDB = workRepository.findById(date).orElse(null);
        if(workFromDB==null)
        {
            workFromDB = new Work();
            workFromDB.addHomework(homeworkFromDB);
            workFromDB.setDate(date);
            workFromDB.setForgingroup(homeworkFromDB.getStudentsByIdPerson().getForgingroup());
            workFromDB.setStatus("+");
        }
        else
        {
            workFromDB.addHomework(homeworkFromDB);
            workFromDB.setForgingroup(homeworkFromDB.getStudentsByIdPerson().getForgingroup());
        }
        homeworkRepository.save(homeworkFromDB);
        workRepository.save(workFromDB);

    }

    public Iterable<Work> findHomeworkByStudent(int idStudents) {
        return  workRepository.findByIdGroup(idStudents);
    }

    public Iterable<Work> findWorkByGroup(int idForginGroup) {
        return  workRepository.findByIdGroup(idForginGroup);
    }

    public Iterable<Work> findWorkByGroupSortByDate(int idForginGroup) {
        return  workRepository.findByIdGroupSortByDate(idForginGroup);
    }

    public Iterable<Work> findWorkByGroupSortByMark(int idForginGroup) {
        return  workRepository.findByIdGroupSortByMark(idForginGroup);
    }
}

package com.example.kursavoy.Service;

import com.example.kursavoy.Model.*;
import com.example.kursavoy.enums.Role;
import com.example.kursavoy.repo.CoursRepository;
import com.example.kursavoy.repo.GroupRepository;
import com.example.kursavoy.repo.WorkRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class GroupService {
    private final WorkRepository workRepository;
    private final GroupRepository groupRepository;
    private final CoursRepository coursRepository;
    public boolean addNewGroup(Forgingroup forgingroup, Cours cours) {
        Forgingroup forgingroupFromDB = groupRepository.save(forgingroup);
        cours.setForgingroup(forgingroup);
        coursRepository.save(cours);
        return true;
    }
    public boolean deleteGroup(int id) {
        Cours cours = coursRepository.findById(id).orElse(null);
        Forgingroup forgingroup = groupRepository.findById(cours.getForgingroup().getIdForginGroup()).orElse(null);
        List<Work> works = workRepository.findByGroup(forgingroup.getIdForginGroup());
        for(int i =0 ;i <works.size(); i++)
        {
            workRepository.delete(works.get(i));
        }
        groupRepository.delete(forgingroup);
        coursRepository.delete(cours);
            return true;
    }

    public void changeCoursOrService(int id, Forgingroup forgingroup, Cours cours) {
        Cours coursFromDB = coursRepository.findById(id).orElse(null);
        Forgingroup forgingroupFromDB = groupRepository.findById(coursFromDB.getForgingroup().getIdForginGroup()).orElse(null);
        if(coursFromDB!=null && forgingroupFromDB!=null)
        {
            if (forgingroupFromDB.getLanguage().equals(forgingroup.getLanguage())==false)
                forgingroupFromDB.setLanguage(forgingroup.getLanguage());
            if (forgingroupFromDB.getColStudent() != forgingroup.getColStudent())
                forgingroupFromDB.setColStudent(forgingroup.getColStudent());
            if(coursFromDB.getNameCours().equals(cours.getNameCours())==false)
                coursFromDB.setNameCours(cours.getNameCours());
            if(coursFromDB.getLevelForCours().equals(cours.getLevelForCours())==false)
                coursFromDB.setLevelForCours(cours.getLevelForCours());
            if (coursFromDB.getInfoCours().equals(cours.getInfoCours())==false)
                coursFromDB.setInfoCours(cours.getInfoCours());

        }
       addNewGroup(forgingroupFromDB, coursFromDB);
    }

    public void deleteFromGroup(int id) {
        Cours cours = coursRepository.findByGroupAndStudent(id);
        Forgingroup forgingroup = groupRepository.findById(cours.getForgingroup().getIdForginGroup()).orElse(null);
        List<Work> works = workRepository.findByGroup(forgingroup.getIdForginGroup());
        for(int i =0 ;i <works.size(); i++)
        {
            workRepository.delete(works.get(i));
        }
        groupRepository.delete(forgingroup);
        coursRepository.delete(cours);


    }

    public List<Forgingroup> findAllAdminsGroup(int idAdmin) {
        return groupRepository.findByAdmin(idAdmin);
    }
}

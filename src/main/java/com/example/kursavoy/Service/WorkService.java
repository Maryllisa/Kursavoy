package com.example.kursavoy.Service;

import com.example.kursavoy.Model.*;
import com.example.kursavoy.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class WorkService {
    private final GroupRepository groupRepository;
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final WorkRepository workRepository;
    private final DocumentRepository documentRepository;

    public Iterable<Work> readHomework() {
        return workRepository.findAll();
    }

    public void addNewWork(int id, Work work) {
        Forgingroup forgingroup = groupRepository.findById(id).orElse(null);
        work.setStatus("+");
        work.setForgingroup(forgingroup);
        workRepository.save(work);
    }

    public void addNewWork(String id, String status, Date date) {
        Students stFromDB = studentRepository.findById(Integer.valueOf(id)).orElse(null);
        Work work = new Work();
        work.setDate(date);
        work.setStatus(status);
    }

    public void addNewWorkStatus(String id, String status, Date date) {
        Work work = workRepository.findById(date).orElse(null);
        Students students = studentRepository.findById(Integer.valueOf(id)).orElse(null);
        Homework homework= null;
        if (work.getHomework().size()!=0) {
            homework = homeworkRepository.findByIdStudentAndWork(Integer.parseInt(id), date);
        }
        if(homework==null)
        {
            homework = new Homework();
            if(status.equals("н")) homework.setMark(-1);
            else if(status.equals("нб")) homework.setMark(0);
            else homework.setMark(Integer.parseInt(status));
            homework.setStudentsByIdPerson(students);
        }
        else {
            if(status.equals("н")) homework.setMark(-1);
            else if(status.equals("нб")) homework.setMark(0);
            else homework.setMark(Integer.parseInt(status));
        }
        work.addHomework(homework);
        homeworkRepository.save(homework);
        workRepository.save(work);
    }

    public Iterable<Work> readHomework(int id) {
        return workRepository.findWorkByGroup(id);
    }

    public List<Work> findWorkForStudent(String surname, String name, String patronymic, String brth, int id) {
        return workRepository.findAllByGroupAndSNP(surname,name,patronymic,brth,id);

    }
    public Document addDocument(MultipartFile file) throws IOException {
        Document document = new Document();
        document.setName(file.getName());
        document.setOriginalFileName(file.getOriginalFilename());
        document.setContentType(file.getContentType());
        document.setSize(file.getSize());
        Base64.Encoder encoder = Base64.getEncoder();
        String encoded = encoder.encodeToString(file.getBytes());
        document.setBytes(encoded);
        documentRepository.save(document);
        return document;
    }
    public void addNewHomework(Date date, int id, MultipartFile file) throws IOException {
        Work workFromDB = workRepository.findById(date).orElse(null);
        Students student = studentRepository.findById(id).orElse(null);
        Homework HomeworkFromDb =null;
        if(workFromDB.getHomework().size()!=0)
        {
            int flag=0;
            for (int i = 0; i < workFromDB.getHomework().size(); i++) {
                if(workFromDB.getHomework().get(i).getStudentsByIdPerson().getIdStudents()==id)
                flag++;
            }
            if(flag!=0) HomeworkFromDb = homeworkRepository.findByIdStudentAndWork(id, date);
        }

        if (HomeworkFromDb == null) {
            Homework homework = new Homework();
            homework.setStudentsByIdPerson(student);
            homework.setDocument(addDocument(file));
            documentRepository.save(homework.getDocument());
            homeworkRepository.save(homework);
            workFromDB.addHomework(homework);
            homeworkRepository.save(homework);
            workRepository.save(workFromDB);
        }
        else {
            HomeworkFromDb.setDocument(addDocument(file));
            documentRepository.save(HomeworkFromDb.getDocument());
            homeworkRepository.save(HomeworkFromDb);
            workFromDB.addHomework(HomeworkFromDb);
            homeworkRepository.save(HomeworkFromDb);
            workRepository.save(workFromDB);
        }

    }

    public Iterable<Work> readDateWork(int id) {
        return workRepository.getHomeworkByAdmin(id);
    }

    public List<Work> readHomeworkSort(int sort, int idForginGroup) {
        List<Work> works = workRepository.findWorkByGroupSort(idForginGroup);
        List<Work> workList = new ArrayList<>();
        if(sort==0) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==1) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==2) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==3) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==4) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==5) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==6) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==7) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else  if(sort==8) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==9) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==10) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        else if(sort==11) {
            for (int i = 0; i < works.size(); i++)
            {
                if (works.get(i).getDate().getMonth()==sort) workList.add(works.get(i));
            }

        }
        return workList;
    }
}

package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.*;
import com.example.kursavoy.Service.CoursService;
import com.example.kursavoy.Service.HomeworkService;
import com.example.kursavoy.Service.StudentService;
import com.example.kursavoy.Service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/user/journal")
@RequiredArgsConstructor
public class JournalStudentController {

    private final CoursService coursService;
    private final StudentService studentService;
    private final HomeworkService homeworkService;
    private final WorkService workService;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate localDate = LocalDate.now();


    @GetMapping("/")
    public String getJournal(Model model)
    {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(student.getForgingroup()!=null) {
            Cours cours = coursService.readCoursByGroup(student.getForgingroup().getIdForginGroup());
            Iterable<Work> works = workService.readHomework(student.getForgingroup().getIdForginGroup());
            model.addAttribute("works", works);
            model.addAttribute("cours", cours);
        }
        else {
            Cours cours = null;
            Work works = null;
            model.addAttribute("cours", cours);
            model.addAttribute("works", works);
        }

        model.addAttribute("student", student);

        return "user/journal";
    }
    @GetMapping("/sort/")
    public String getJournalSort(@RequestParam String sort, Model model)
    {
        int numMonth = Integer.parseInt(sort);
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cours cours = coursService.readCoursByGroup(student.getForgingroup().getIdForginGroup());
        Iterable<Work> works = workService.readHomeworkSort(numMonth, student.getForgingroup().getIdForginGroup());
        model.addAttribute("student", student);
        model.addAttribute("cours", cours);
        model.addAttribute("works", works);
        return "user/journal";
    }
}

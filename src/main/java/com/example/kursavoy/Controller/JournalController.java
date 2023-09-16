package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.*;
import com.example.kursavoy.Service.CoursService;
import com.example.kursavoy.Service.HomeworkService;
import com.example.kursavoy.Service.StudentService;
import com.example.kursavoy.Service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/admin/journal")
@RequiredArgsConstructor
public class JournalController {

    private final CoursService coursService;
    private final StudentService studentService;
    private final HomeworkService homeworkService;
    private final WorkService workService;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate localDate = LocalDate.now();


    @GetMapping("/")
    public String getJournal(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Cours> cours = coursService.readCoursAdmin(admin.getIdAdmin());
        model.addAttribute("localDate", localDate);
        model.addAttribute("admin", admin);
        model.addAttribute("cours", cours);
        return "admin/journal_head";
    }
    @GetMapping("/finder")
    public String getFinder(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("admin", admin);
        model.addAttribute("localDate", localDate);
        return "finder";
    }
    @GetMapping("/journals/{id}")
    public String getJournal(@PathVariable int id, Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cours cours = coursService.readCoursByGroup(id);
        Iterable<Work> works = workService.readHomework(id);
        model.addAttribute("admin", admin);
        model.addAttribute("cours", cours);
        model.addAttribute("works", works);
        return "journal";
    }
    @GetMapping("/journals/sort/{id}")
    public String getJournalSort(@PathVariable int id, @RequestParam String sort, Model model)
    {
        int numberMonth = Integer.parseInt(sort);
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cours cours = coursService.readCoursByGroup(id);
        Iterable<Work> works = workService.readHomeworkSort(numberMonth, id);
        model.addAttribute("admin", admin);
        model.addAttribute("cours", cours);
        model.addAttribute("works", works);
        return "journal";
    }
    @PostMapping("/add_work")
    public String addWork(@RequestParam String id,
                          @RequestParam String status,
                          @RequestParam Date date,
                          Model model)
    {

        workService.addNewWork(id, status, date);
        model.addAttribute("localDate", localDate);
        return "redirect:/admin/journal/";
    }
    @PostMapping("/add_status")
    public String addWorkStatus(@RequestParam String id,
                          @RequestParam String status,
                          @RequestParam Date date,
                          Model model)
    {
        workService.addNewWorkStatus(id, status, date);
        model.addAttribute("localDate", localDate);
        return "redirect:/admin/journal/";
    }
    @PostMapping("/add_work_date/{id}")
    public String addWork(@PathVariable int id,@RequestParam Date date, Model model)
    {
        Work work = new Work();
        work.setDate(date);
        workService.addNewWork(id, work);
        model.addAttribute("localDate", localDate);
        return "redirect:/admin/journal/";
    }
    @PostMapping("/finder/surch/{id}")
    public String getFinder(@PathVariable int id,
                            @RequestParam String name,
                            @RequestParam String surname,
                            @RequestParam String patronymic,
                            @RequestParam String brth,
                            Model model)
    {///поиск с условияям на null
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cours cours = coursService.readCoursByGroup(id);
        List<Work> works = workService.findWorkForStudent(surname, name, patronymic, brth, id);
        List<Students> students = studentService.findStudent(surname,name,patronymic,brth);
        model.addAttribute("localDate", localDate);
        model.addAttribute("admin", admin);
        model.addAttribute("cours", cours);
        model.addAttribute("students", students);
        model.addAttribute("works", works);

        return "admin/finder";
    }
}

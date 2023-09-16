package com.example.kursavoy.Controller;


import com.example.kursavoy.Model.*;
import com.example.kursavoy.Service.HomeworkService;
import com.example.kursavoy.Service.WorkService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Controller
@RequestMapping("/user/homework")
@RequiredArgsConstructor
public class HomeworkStudentController {
    private final HomeworkService homeworkService;
    private final WorkService workService;
    @GetMapping("/")
    public String getMenuHomework(Model model) {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(student.getForgingroup()!=null) {
            Iterable<Work> works = homeworkService.findWorkByGroup(student.getForgingroup().getIdForginGroup());
            model.addAttribute("works", works);
        }
        else {
            Work work = null;
            model.addAttribute("works", work);
        }
        model.addAttribute("student", student);
        return "user/homework/homework";
    }
    @GetMapping("/sort/")
    public String sortByParam(@RequestParam String sort, Model model)
    {
        if (sort.equals("sortByDate")) {
            Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Iterable<Work> works = homeworkService.findWorkByGroupSortByDate(student.getForgingroup().getIdForginGroup());
            model.addAttribute("student", student);
            model.addAttribute("works", works);
        }
        else if (sort.equals("sortByMark"))
        {
            Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Iterable<Work> works = homeworkService.findWorkByGroupSortByMark(student.getForgingroup().getIdForginGroup());
            model.addAttribute("student", student);
            model.addAttribute("works", works);
        }
        else return "redirect:/user/homework/";
        return "user/homework/homework";
    }
    @SneakyThrows
    @PostMapping("/add/{date}/{id}")
    public String addHomework(@PathVariable Date date,
                              @PathVariable int id,
                              @RequestParam MultipartFile file,
                              Model model)
    {
        workService.addNewHomework(date, id, file);
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Work> works = homeworkService.findHomeworkByStudent(student.getIdStudents());
        model.addAttribute("student", student);
        model.addAttribute("works", works);
        return "redirect:/user/homework/";
    }

}

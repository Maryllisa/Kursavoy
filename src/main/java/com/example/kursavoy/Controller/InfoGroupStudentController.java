package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.Cours;
import com.example.kursavoy.Model.Image;
import com.example.kursavoy.Model.Students;
import com.example.kursavoy.Service.CoursService;
import com.example.kursavoy.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Controller
@RequestMapping("/user/group")
@RequiredArgsConstructor
public class InfoGroupStudentController {
    private final StudentService studentsService;
    private final CoursService coursService;

    @GetMapping("/")
    public String getInfoAboutGroup(Model model) {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cours cours = null;
        if (student.getForgingroup()!=null) {
             cours = coursService.findCoursByGroup(student.getForgingroup().getIdForginGroup());
        }
        model.addAttribute("student", student);
        model.addAttribute("cours", cours);
        return "user/group/info-about-group";
    }
    @GetMapping("/cheak_student/{id}")
    private String getCheakStudent(@PathVariable int id, Model model)
    {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Students> students = studentsService.readStudentByGroup(student.getForgingroup().getIdForginGroup());
        Cours cours = coursService.findCoursByGroup(student.getForgingroup().getIdForginGroup());
        model.addAttribute("cours", cours);
        model.addAttribute("student", student);
        model.addAttribute("students", students);
        return "user/group/info-about-group-all";
    }
}

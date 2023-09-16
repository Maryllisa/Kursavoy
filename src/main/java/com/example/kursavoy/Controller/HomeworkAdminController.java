package com.example.kursavoy.Controller;


import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Homework;
import com.example.kursavoy.Model.Work;
import com.example.kursavoy.Service.HomeworkService;
import com.example.kursavoy.Service.WorkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@Controller
@RequestMapping("/admin/cheak_homework")
@RequiredArgsConstructor
public class HomeworkAdminController {
    private final HomeworkService homeworkService;
    private final WorkService workService;
    @GetMapping("/")
    public String getMenuHomework(Model model) {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Homework> homework = homeworkService.readHomewokByAdmin(admin.getIdAdmin());
        Iterable<Work> works =  workService.readDateWork(admin.getIdAdmin());
        model.addAttribute("admin", admin);
        model.addAttribute("homework", homework);
        model.addAttribute("cours", works);
        return "admin/cheak_homework";
    }
    @PostMapping("/mark/{id}")
    public String addMark(@PathVariable Long id, @RequestParam String mark,
                          @RequestParam Date date, Model model)
    {
        homeworkService.addMark(id, Integer.parseInt(mark), date);
        return "redirect:/admin/cheak_homework/";
    }
}

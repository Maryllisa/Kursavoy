package com.example.kursavoy.Controller;


import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Fee;
import com.example.kursavoy.Model.Forgingroup;
import com.example.kursavoy.Model.Students;
import com.example.kursavoy.Service.FeeService;
import com.example.kursavoy.Service.GroupService;
import com.example.kursavoy.Service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user/fee")
public class FeeStudentController {
    private final FeeService feeService;
    private final GroupService groupService;
    private final StudentService studentService;

    @GetMapping("/")
    public String getPageFee(Model model)
    {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Fee> fees = feeService.readByStudent(student.getIdStudents());
        model.addAttribute("student", student);
        model.addAttribute("fees", fees);
        return "user/fee";
    }
    @GetMapping("/sort/")
    public String getPageFeeSort(@RequestParam String sort, Model model)
    {
       if(sort.equals("sortByDateAsc"))
       {
           Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
           Iterable<Fee> fees = feeService.readByStudentByDateAsc(student.getIdStudents());
           model.addAttribute("student", student);
           model.addAttribute("fees", fees);
           return "user/fee";
       }
       else if (sort.equals("sortByDateDesc")) {
           Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
           Iterable<Fee> fees = feeService.readByStudentByDateDesc(student.getIdStudents());
           model.addAttribute("student", student);
           model.addAttribute("fees", fees);
           return "user/fee";
       }
       else return "redirect:/user/fee/";
    }

    @SneakyThrows
    @PostMapping("/add/")
    public String addFee(@RequestParam Date date, @RequestParam MultipartFile file, Model model)
    {
        Students student = (Students) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        feeService.addFee(student.getIdStudents(), date, file);
        Iterable<Fee> fees = feeService.readByStudent(student.getIdStudents());
        model.addAttribute("student", student);
        model.addAttribute("fees", fees);
        return "redirect:/user/fee/";
    }


}

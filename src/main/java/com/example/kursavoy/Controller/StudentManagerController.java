package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Blog;
import com.example.kursavoy.Model.Forgingroup;
import com.example.kursavoy.Model.Students;
import com.example.kursavoy.Service.GroupService;
import com.example.kursavoy.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/manager")
public class StudentManagerController {
    public final StudentService studentService;
    public final GroupService groupService;
    @GetMapping("/")
    public String getManagerPage(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Forgingroup> forgingroupList = groupService.findAllAdminsGroup(admin.getIdAdmin());
        int length = forgingroupList.size();
        int []gr = new int[length];
        for (int i = 0; i < length; i++) {
            gr[i] = forgingroupList.get(i).getIdForginGroup();
        }
        Iterable<Students> students = studentService.readAllStudentByGroup(gr);
        model.addAttribute("admin", admin);
        model.addAttribute("student", students);
        return "/admin/StudentManager";
    }
    @GetMapping("/delete/{id}")
    public String deletUser(@PathVariable int id, Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        studentService.deleteStudent(id);
        model.addAttribute("admin", admin);
        return "redirect:/admin/manager/";
    }
    @GetMapping("/change/{id}")
    public String profile(@PathVariable int id,
                          Model model) {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Students students = studentService.findStudent(id);
        if(students!=null) model.addAttribute("student", students);
        model.addAttribute("admin", admin);
        return "/admin/changeStudentProfile";
    }
    @PostMapping("/change/{id}")
    public String changeUser( @PathVariable int id,
                              @RequestParam String login,
                              @RequestParam String password,
                              @RequestParam String surname,
                              @RequestParam String name,
                              @RequestParam String partonymic,
                              @RequestParam String email,
                              @RequestParam String bth,
                              @RequestParam String level,
                              @RequestParam("file") MultipartFile file,
                              Model model)  throws IOException

        {
            Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Students student = new Students();
        student.setIdStudents(id);
        student.setLogin(login);
        student.setPassword(password);
        student.setSurname(surname);
        student.setName(name);
        student.setPatronymic(partonymic);
        student.setEmail(email);
        student.setBrth(bth);
        student.setLevel(level);
        studentService.changeStudent(student, file);
            model.addAttribute("admin", admin);
        return "redirect:/admin/manager/";
    }
    @GetMapping("/block/{id}")
    public String blockUser(@PathVariable int id, Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        studentService.changeAction(id);
        model.addAttribute("admin", admin);
        return "redirect:/admin/manager/";
    }
}

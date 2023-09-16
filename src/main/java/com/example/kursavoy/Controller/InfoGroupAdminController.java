package com.example.kursavoy.Controller;

import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Cours;
import com.example.kursavoy.Model.Forgingroup;
import com.example.kursavoy.Model.Students;
import com.example.kursavoy.Service.AdminService;
import com.example.kursavoy.Service.CoursService;
import com.example.kursavoy.Service.GroupService;
import com.example.kursavoy.Service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/group")
@RequiredArgsConstructor
public class InfoGroupAdminController {
    private final GroupService groupService;
    private final CoursService coursService;
    private final AdminService adminService;
    private final StudentService studentService;
    @GetMapping("/")  //группы
    public String getGroup(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Cours> cours = coursService.readCours();
        model.addAttribute("cours", cours);
        model.addAttribute("admin", admin);
        return "admin/group/info-group";
    }
    @GetMapping("/myCours")  //группы
    public String getMyCours(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Cours> cours = coursService.readCoursAdmin(admin.getIdAdmin());
        model.addAttribute("admin", admin);
        model.addAttribute("cours", cours);
        return "admin/group/myGroup";
    }

    @GetMapping("/info-group-student") //распределение студентов
    public String getStudentGroup(Model model)
    {
        Iterable<Students> studentsWithoutGroup = studentService.findByWithoutGroup();
        Iterable<Students> students = studentService.findByWithGroup();
        Iterable<Cours> cours = coursService.readCours();
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("students",students);
        model.addAttribute("studentsWithoutGroup",studentsWithoutGroup);
        model.addAttribute("cours",cours);
        model.addAttribute("admin", admin);
        return "admin/group/info-group-student";
    }
    @GetMapping("/add_teach_group") // ведение групп
    public String getSetNewTeachForGroup(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Iterable<Cours> cours = coursService.findCoursWithoutAdmin();
        Iterable<Admim> admins  = adminService.readAdmin();
        model.addAttribute("admin", admin);
        model.addAttribute("admins", admins);
        model.addAttribute("cours", cours);
        return "admin/group/add_teach_group";
    }

    @PostMapping("/add_new_group") // добавление новой группы
    public String setAddNewGroup(@RequestParam String language,
                                 @RequestParam String col_student,
                                 @RequestParam String info_about_cours,
                                 @RequestParam String level_for_cours,
                                 @RequestParam String name_cours,
                                 Model model)
    {
        Forgingroup forgingroup = new Forgingroup();
        forgingroup.setLanguage(language);
        forgingroup.setColStudent(Integer.parseInt(col_student));
        Cours cours = new Cours();
        cours.setInfoCours(info_about_cours);
        cours.setNameCours(name_cours);
        cours.setLevelForCours(level_for_cours);
        groupService.addNewGroup(forgingroup, cours);
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("admin", admin);
        return "redirect:/admin/group/";
    }
    @PostMapping("/change/{id}") // изменение группы
    public String getChangeGroup(@PathVariable int id,
                                 @RequestParam String language,
                                 @RequestParam String col_student,
                                 @RequestParam String info_about_cours,
                                 @RequestParam String level_for_cours,
                                 @RequestParam String name_cours, Model model)
    {
        Forgingroup forgingroup = new Forgingroup();
        forgingroup.setLanguage(language);
        forgingroup.setColStudent(Integer.parseInt(col_student));
        Cours cours = new Cours();
        cours.setInfoCours(info_about_cours);
        cours.setNameCours(name_cours);
        cours.setLevelForCours(level_for_cours);
        groupService.changeCoursOrService( id, forgingroup, cours);
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("admin", admin);
        return "redirect:/admin/group/";
    }

    @GetMapping("/delete/{id}") // удаление группы
    public String getDeleteGroup(@PathVariable int id, Model model)
    {
        groupService.deleteGroup(id);
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("admin", admin);
        return "redirect:/admin/group/";
    }
    @GetMapping("/delete_from_group/{id}") // удаление группы
    public String getDeleteFromGroup(@PathVariable int id, Model model)
    {
        studentService.deleteGroup(id);
//        groupService.deleteFromGroup(id);
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("admin", admin);
        return "redirect:/admin/group/info-group-student";
    }
    @PostMapping("/add_new_student/{id}") // изменение группы
    public String addStudentInGroup(@PathVariable int id,
                                 @RequestParam String group,Model model)
    {
        studentService.addStudentInGroup(id, group);
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("admin", admin);
        return "redirect:/admin/group/info-group-student";
    }
    @PostMapping("/add_teacher/{id}") // изменение группы
    public String addAdminInGroup(@PathVariable int id,Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        adminService.addNewGroup(id, admin.getIdAdmin());
        model.addAttribute("admin", admin);
        return "redirect:/admin/group/add_teach_group";
    }
    @PostMapping("/delete_admin/{id}") // изменение группы
    public String deleteAdminInGroup(@PathVariable int id,Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        adminService.deleteGroup(id);
        model.addAttribute("admin", admin);
        return "redirect:/admin/group/myCours";
    }

}

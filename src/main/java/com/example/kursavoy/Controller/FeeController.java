package com.example.kursavoy.Controller;


import com.example.kursavoy.Model.Admim;
import com.example.kursavoy.Model.Fee;
import com.example.kursavoy.Model.Forgingroup;
import com.example.kursavoy.Service.FeeService;
import com.example.kursavoy.Service.GroupService;
import com.example.kursavoy.Service.StudentService;
import com.example.kursavoy.repo.GroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/fee")
public class FeeController {
    private final FeeService feeService;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate localDate = LocalDate.now();
    private final GroupService groupService;
    private final StudentService studentService;

    @GetMapping("/")
    public String getPageFee(Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Forgingroup> forgingroupList = groupService.findAllAdminsGroup(admin.getIdAdmin());
        int length = forgingroupList.size();
        int []gr = new int[length];
        for (int i = 0; i < length; i++) {
            gr[i] = forgingroupList.get(i).getIdForginGroup();
        }
        List<Fee> fees = (List<Fee>) feeService.readAllFeeByGroup(gr);
        model.addAttribute("localDate", localDate);
        model.addAttribute("admin", admin);
        model.addAttribute("fees", fees);
        return "fee";
    }
    @GetMapping("/findFee/")
    public String getPageFeeFind(@RequestParam String surname,
                                 @RequestParam String name,
                                 @RequestParam String patronymic,
                                 Model model)
    {
        Admim admin = (Admim) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Forgingroup> forgingroupList = groupService.findAllAdminsGroup(admin.getIdAdmin());
        int length = forgingroupList.size();
        int []gr = new int[length];
        for (int i = 0; i < length; i++) {
            gr[i] = forgingroupList.get(i).getIdForginGroup();
        }
        List<Fee> fees = (List<Fee>) feeService.readAllFeeByGroupBySNP(gr, surname, name,patronymic);
        model.addAttribute("localDate", localDate);
        model.addAttribute("admin", admin);
        model.addAttribute("fees", fees);
        return "fee";
    }
    @GetMapping("/false/{id}/{id_fee}")
    public String getNotStatusFalse(@PathVariable int id,
                               @PathVariable long id_fee,
                               Model model)
    {
        studentService.changeStatusfeeFalse(id, id_fee);
        return "redirect:/admin/fee/";
    }
    @GetMapping("/true/{id}/{id_fee}")
    public String getNotStatusTrue(@PathVariable int id,
                               @PathVariable long id_fee,
                               Model model)
    {
        studentService.changeStatusfeeTrue(id, id_fee);
        return "redirect:/admin/fee/";
    }
}
